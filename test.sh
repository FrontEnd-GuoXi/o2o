echo "${date}"
echo "${WORKSPACE}"
rmStr=""
cd /guazai/clone_repository/udap_rep/acm-web_dev || exit
for commit_id in $(/usr/local/git/bin/git log --after="${date} 00:00:00" --pretty=format:"%H"); do
  for filename in $(/usr/local/git/bin/git diff-tree --no-commit-id --name-only -r "$commit_id"); do
    filename="./"$filename
    echo "$filename"
    folder="./"${filename}
    folder="./acm-web/"${folder%/*}
   
    if [ -d "$folder" ]; then
      echo "$folder""目录存在不需要创建！"
    else
      echo "$folder""目录不存在，准备创建"
      mkdir -p "$folder"
      echo "$folder""目录创建完成！"
    fi
    if [ ! -e "$filename" ]; then
      echo "filename源文件不存在，记录到删除脚本中！"
      rmStr+=" rm -f ${filename}\n" 
      continue
    fi
    cp "$filename" "$folder"
  done
done
if [ ! -z "$rmStr" ]; then
echo -e "$rmStr" > ./acm-web/acm-web_remove.sh
fi
cd "${WORKSPACE}" || exit
rm -rf ./acm-web
cd /guazai/clone_repository/udap_rep/acm-web_dev || exit
mv ./acm-web "${WORKSPACE}"
