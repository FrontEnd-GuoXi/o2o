import * as fs from 'fs';
import * as path from 'path';

// 定义要重命名的文件夹
const foldersToRename = [
  { oldName: 'Login', newName: 'o2oLogin' },
  { oldName: 'Register', newName: 'o2oRegister' }
];

// 重命名文件夹
foldersToRename.forEach(({ oldName, newName }) => {
  const oldPath = path.join(process.cwd(), 'src', 'pages', oldName);
  const newPath = path.join(process.cwd(), 'src', 'pages', newName);

  try {
    if (fs.existsSync(oldPath)) {
      fs.renameSync(oldPath, newPath);
      console.log(`成功重命名文件夹: ${oldName} -> ${newName}`);
    } else {
      console.log(`文件夹不存在: ${oldPath}`);
    }
  } catch (error) {
    console.error(`重命名文件夹失败: ${oldName}`, error);
  }
});
