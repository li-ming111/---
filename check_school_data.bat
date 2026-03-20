@echo off
mysql -u root -p123456 -e "USE xueya; SELECT * FROM school;"
echo 查询完成
pause
