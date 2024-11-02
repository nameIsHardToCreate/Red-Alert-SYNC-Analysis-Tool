<template>
  <div id="main">
    <div id="upload">
  <a-upload-dragger
    v-model:fileList="fileList"
    name="file"
    :multiple="true"
    :before-upload="beforeUpload"
    :custom-request="customRequest"
    @change="handleChange"
    @drop="handleDrop"
  >
    <p class="ant-upload-drag-icon">
      <inbox-outlined></inbox-outlined>
    </p>
    <p class="ant-upload-text">点击或拖拽 .txt 文件到此区域进行上传</p>
    <p class="ant-upload-hint">
      支持单个或多个文件上传。严格禁止上传公司数据或其他禁止文件。
    </p>
  </a-upload-dragger>
</div>
<div class="button-container">
      <a-button class="centered-button" type="primary" @click="Ok">
        确定
      </a-button>
    </div>
</div>
<!--把表格放下面-->

  <table>
    <thead>
    <tr>
      <th></th>
      <th>{{ fileName1 }}</th>
      <th>{{ fileName2 }}</th>
    </tr>
    </thead>
    <tbody>
    <tr v-for="row in tableData" :key="row.key">
      <td>{{ row.key }}</td>
      <td v-html="row.value1"></td> <!-- 使用 v-html 渲染 HTML -->
      <td v-html="row.value2"></td> <!-- 使用 v-html 渲染 HTML -->
    </tr>
    </tbody>
  </table>


  <div style="display: flex; justify-content: space-between;">
    <!-- 区域1: 显示Key -->
    <div>
      <h3>区域1（Key）:</h3>
      <p v-for="(value, key) in filenameMap2" :key="key">{{ key }}</p>
      <p v-for="(value, key) in filenameMaps" :key="key" v-html="highlightedValues2[key]"></p>
    </div>

    <!-- 区域2: 显示Value -->
    <div>
      <h3>区域2（Value）:</h3>
      <p v-for="(value, key) in filenameMap2" :key="key">{{ value }}</p>
      <p v-for="(value, key) in filenameMaps" :key="key" v-html="highlightedValues[key]"></p>
    </div>
  </div>






</template>

<script setup>
import { ref, onMounted } from 'vue';
import { message } from 'ant-design-vue';
import axios from 'axios';

const fileList = ref([]);
const tableData = ref([]);
const columns = ref([]);
const filenameMap2 = ref([]);
const filenameMaps = ref({});
const highlightedValues = ref([]);
const highlightedValues2 = ref([]);
const Ok = () => {
  tableData.value = [];
  columns.value = [];
  const formData = new FormData();
  Object.entries(localStorage).forEach(([key, value]) => {
    formData.append(key, value);
  });

  const formDataList = Array.from(formData.entries()).map(([name, values]) => {
    return { name, values };
  });

  axios.post("api/analyze", formDataList)
      .then(response => {
        message.success("请求成功！");
        console.log("这里是数据", response.data);

        const { fileNameMap, differentLine, differentLine2, filenameMap2 } = response.data;
        filenameMap2.value = filenameMap2;

        // 清理数据
        const cleanedData = differentLine.map(line => line.replace(/\r/g, '').trim());
        const keys = Array.from(new Set(cleanedData.map(line => line.split(':')[0].trim())));
        const values = cleanedData.map(line => line.split(':')[1]?.trim() || '');

        // 获取文件名
        const fileNames = Object.keys(fileNameMap);
        const fileName1 = fileNames[0];
        const fileName2 = fileNameMap[fileName1];

        if (Array.isArray(differentLine2)) {
          const map = {};
          highlightedValues.value = {}; // 清空高亮值
          highlightedValues2.value ={};
          // 构建键值对并高亮比较
          for (let i = 0; i < differentLine2.length; i += 2) {
            const key = differentLine2[i];
            const value = differentLine2[i + 1];

            if (key !== undefined && value !== undefined) {
              map[key] = value;
              highlightedValues.value[key] = compareAndHighlight(key, value); // 存储高亮的结果
              highlightedValues2.value[key] = compareAndHighlight3(value, key);
            }
          }
          filenameMaps.value = map; // 更新 ref 的值
        }

        // 创建表格的列
        columns.value = [
          { title: '', dataIndex: 'key', key: 'key' },
          { title: fileName1, dataIndex: 'value1', key: 'value1' },
          { title: fileName2, dataIndex: 'value2', key: 'value2' },
        ];

        // 创建表格数据
        const tableRows = keys.map((key, index) => {
          const row = {
            key,
            value1: values[index * 2] || '', // 第一个值
            value2: values[index * 2 + 1] || '', // 第二个值
          };
          const { result1, result2 } = compareAndHighlight2(row.value1, row.value2);
          row.value1 = result1; // 用高亮后的值替换
          row.value2 = result2; // 用高亮后的值替换
          return row;
        });

        tableData.value = tableRows; // 更新表格数据
      })
      .catch(error => {
        console.error("请求失败:", error);
        message.error("请求失败，请重试！");
      });
};





function compareAndHighlight3(key, value) {
  let result = '';
  const maxLength = Math.max(key.length, value.length);

  for (let i = 0; i < maxLength; i++) {
    const keyChar = key[i] || '';
    const valueChar = value[i] || '';

    if (keyChar === valueChar) {
      result += keyChar; // 相同字符
    } else if (valueChar) {
      result += `<span style="color: red;">${valueChar}</span>`; // 不同字符变红
    }
  }

  return result;
}






function compareAndHighlight(key, value) {
  let result = '';
  let result2 ='';
  const maxLength = Math.max(key.length, value.length);

  for (let i = 0; i < maxLength; i++) {
    const keyChar = key[i] || '';
    const valueChar = value[i] || '';

    if (keyChar === valueChar) {
      result += keyChar; // 相同字符
    } else if (valueChar) {
      result += `<span style="color: red;">${valueChar}</span>`; // 不同字符变红
    }
  }

  return result;
}

// 逐字比较函数
function compareAndHighlight2(value1, value2) {
  let result1 = '';
  let result2 = '';
  const maxLength = Math.max(value1.length, value2.length);

  for (let i = 0; i < maxLength; i++) {
    const char1 = value1[i] || '';
    const char2 = value2[i] || '';

    if (char1 === char2) {
      result1 += char1; // 相同字符
      result2 += char2; // 相同字符
    } else {
      result1 += `<span style="color: red;">${char1}</span>`; // 不同字符标红
      result2 += `<span style="color: red;">${char2}</span>`; // 不同字符标红
    }
  }

  return { result1, result2 };
}

// 清空 localStorage
const clearLocalStorage = () => {
  localStorage.clear();
};

// 检查文件类型，确保是 .txt 文件
const beforeUpload = (file) => {
  const isTxt = file.type === 'text/plain';
  if (!isTxt) {
    message.error('只支持上传 .txt 文件!');
  }
  return isTxt;
};

// 自定义请求处理，模拟文件上传
const customRequest = async ({ file, onSuccess }) => {
  // 模拟异步上传，实际上这里应该是你的上传逻辑
  setTimeout(() => {
    onSuccess(file);
    handleFileContent(file); // 处理文件内容
  }, 1000);
};

const handleChange = info => {
  const status = info.file.status;
  
  // 更新 fileList
  fileList.value = info.fileList;

  if (status === 'done') {
    message.success(`${info.file.name} 上传成功.`);
  } else if (status === 'error') {
    message.error(`${info.file.name} 上传失败`);
  }
};

const handleFileContent = (file) => {
  console.log("收到了文件",file)
  // 使用 FileReader 读取文件内容
  const reader = new FileReader();
  // 当文件读取完成后触发
  reader.onload = (event) => {
    const fileContent = event.target.result; // 获取文件内容
    // 将文件内容保存到 localStorage 中，键为文件名，值为文件内容
    localStorage.setItem(`file_${file.name}`, fileContent);

    console.log(`文件内容已保存: ${file.name}`);
    console.log('当前文件列表:');

    // 打印当前文件列表
    for (let i = 0; i < localStorage.length; i++) {
      const key = localStorage.key(i);
      console.log(key);
    }
  };

  // 读取文件内容为文本
  reader.readAsText(file);
};



function handleDrop(e) {
  console.log(e);
}

// 在组件加载时调用 clearLocalStorage
onMounted(() => {
  clearLocalStorage();
});
</script>

<style>
/* 可根据需要添加样式 */
.main-container {
  display: flex;
  flex-direction: column; /* 纵向排列 */
  align-items: center; /* 水平居中 */
  justify-content: center; /* 垂直居中 */
  height: 100vh; /* 视口高度 */
}

.upload-container {
  margin-bottom: 20px; /* 上传区域与按钮之间的间距 */
}

.button-container {
  display: flex;
  justify-content: center; /* 水平居中按钮 */
  width: 100%; /* 确保按钮容器宽度填满 */
}
</style>
