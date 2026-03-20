@echo off
mysql -u root -p123456 -e "USE xueya; SELECT * FROM user WHERE username = '2023020616' OR student_id = '2023020616';"
echo 查询完成
pause
