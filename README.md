# ĐỒ ÁN TỐT NGHIỆP

## Link git code
https://gitlab.com/nongoanh2000/datn?fbclid=IwAR29WdYvv8kqirw__kRJOfTRXqgdH_GET6lEEDUpWTCu1BWg5sX1Y0a__1c

## Hướng dẫn cài đặt
1. Cài đặt Intellij Idea
- Bước 1: 
  - Tải Intellij Idea bản Ultimate tại https://www.jetbrains.com/idea/download/#section=windows
- Bước 2:
  - Kích hoạt tài khoản Intellij bản free cho sinh viên thì cần thực hiện thao tác sau:
    - Đăng nhập tài khoản trên JetBrains Account bằng Email của trường cấp (VD: abc@sis.hust.edu.vn)
    - Sau khi đăng nhập chọn mục " Apply for a free student or teacher license for educational purposes ".Sau khi chọn xong thì kéo xuống dưới chọn " Apply now " để đăng ký Lience
    - Điền đầy đủ thông tin rồi chọn " Apply for free Products " (Lưu ý địa chỉ Email phải điền địa chỉ Email tài khoản nhà trường cấp). Sau đó vào Outlook kích hoạt Lience trong mail bên JetBrains gửi cho.
- Bước 3:
  - Mở file "ideaIU.exe" thực hiện cài đặt vào máy. Sau khi cài đặt thành công mở Intellij Idea Ultimate thì sẽ hiện ra "Lience". Chọn " Active Intellij Idea" và "JB Account" rồi đăng nhập tài khoản vừa đăng ký trên 

2. Cài đặt MySQl
- Bước 1:
  - Cài đặt MySQL Installer for Windows tại https://dev.mysql.com/downloads/file/?id=516927  .Sau đó chọn "No thanks, just start my download" để thực hiện download file cài đặt.
- Bước 2:
  - Mở file cài đặt MySQL Installer để thực hiện cài đặt vào máy, chi tiết cụ thể từng bước tham khảo tại https://www.youtube.com/watch?v=BpwaqlWEnUY&t=150s
  
## Cách sử dụng
- Bước 1: Vào link git code và thực hiện clone code về máy tính trên Git Bash (Nếu chưa có Git thì có thể xem tại https://www.youtube.com/watch?v=lFiceMCN0Ns để thực hiện cài đặt) bằng câu lệnh sau:
  ```bash
  git clone https://gitlab.com/nongoanh2000/datn.git
  ```
- Bước 2: Tạo các bảng cơ sở dữ liệu với các thuộc tính tương ứng trên MySQL
- Bước 3: Thực hiện thay đổi kết nối với cơ sở dữ liệu với url, username và password tương ứng với tài khoản trên MySQL khi thực hiện các thao tách sau:
  - Chọn lần lượt các thư mục src -> resources -> application.properties
  - Thực hiện thay đổi url, username, password tương ứng với tài khoản tạo ở MySQL
- Bước 4: Thực hiện Run code
- Bước 5: Gõ tên miền localhost:8086 (8086 là giá trị server.port lưu trong application.properties)
   
