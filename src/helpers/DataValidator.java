package helpers;

import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dao.KhachHangDAO;
import entity.KhachHang;

public class DataValidator {

    /**
     * kiểm tra trường người dùng nhập vào có rỗng hay không (TextField)
     * 
     * @param field        chứa trường người dùng nhập vào
     * @param sb           đối tượng StringBuilder để append thông điệp lỗi
     * @param errorMessage chứa thông điệp lỗi
     */
    public static void validateEmpty(TextField field, StringBuilder sb, String errorMessage) {
        if (field.getText().isEmpty()) {
            sb.append(errorMessage).append("\n");
            field.requestFocus();
        }
    }

    /**
     * kiểm tra trường mật khẩu người dùng nhập vào có rỗng hay không (PasswordField)
     * 
     * @param field        chứa trường password người dùng nhập vào
     * @param sb           đối tượng StringBuilder để append thông điệp lỗi
     * @param errorMessage chứa thông điệp lỗi
     */
    public static void validateEmpty(PasswordField field, StringBuilder sb, String errorMessage) {
        String password = field.getText();
        if (password.isEmpty()) {
            sb.append(errorMessage).append("\n");
            field.requestFocus();
        }
    }

    /**
     * kiểm tra trường nhập liệu có dấu nhưng không được nhập số
     * 
     * @param field        chứa trường người dùng nhập vào
     * @param sb           đối tượng StringBuilder để append thông điệp lỗi
     * @param errorMessage chứa thông điệp lỗi
     */
    public static void validateVietnameseCharacters(TextField field, StringBuilder sb, String errorMessage) {
        String VIETNAMESE_DIACRITIC_CHARACTERS = "ẮẰẲẴẶĂẤẦẨẪẬÂÁÀÃẢẠĐẾỀỂỄỆÊÉÈẺẼẸÍÌỈĨỊỐỒỔỖỘÔỚỜỞỠỢƠÓÒÕỎỌỨỪỬỮỰƯÚÙỦŨỤÝỲỶỸỴ";

        Pattern p = Pattern.compile("(?:[" + VIETNAMESE_DIACRITIC_CHARACTERS + "]|[A-Z ])++",
                Pattern.CANON_EQ | Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
        Matcher m = p.matcher(field.getText());
        if (!m.matches()) {
            sb.append(errorMessage).append("\n");
            field.requestFocus();
            field.selectAll();
        }
    }

    /**
     * kiểm tra trường nhập liệu có dấu nhưng được nhập số
     * 
     * @param field        chứa trường người dùng nhập vào
     * @param sb           đối tượng StringBuilder để append thông điệp lỗi
     * @param errorMessage chứa thông điệp lỗi
     */
    public static void validateVietnameseCharactersAndNumber(TextField field, StringBuilder sb, String errorMessage) {
        String VIETNAMESE_DIACRITIC_CHARACTERS = "ẮẰẲẴẶĂẤẦẨẪẬÂÁÀÃẢẠĐẾỀỂỄỆÊÉÈẺẼẸÍÌỈĨỊỐỒỔỖỘÔỚỜỞỠỢƠÓÒÕỎỌỨỪỬỮỰƯÚÙỦŨỤÝỲỶỸỴ";

        Pattern p = Pattern.compile("(?:[" + VIETNAMESE_DIACRITIC_CHARACTERS + "]|[A-Z0-9 ])++",
                Pattern.CANON_EQ | Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
        Matcher m = p.matcher(field.getText());
        if (!m.matches()) {
            sb.append(errorMessage).append("\n");
            field.requestFocus();
            field.selectAll();
        }
    }

    /**
     * kiểm tra trường email người dùng nhập vào đúng hay không
     * 
     * @param field        chứa trường email người dùng nhập vào
     * @param sb           đối tượng StringBuilder để append thông điệp lỗi
     * @param errorMessage chứa thông điệp lỗi
     */
    public static void validateEmail(TextField field, StringBuilder sb, String errorMessage) {
        String regex = "\\w+@\\w+(\\.\\w+){1,2}";
        if (!field.getText().matches(regex)) {
            sb.append(errorMessage).append("\n");
            field.requestFocus();
            field.selectAll();
        }
    }

    /**
     * kiểm tra trường số điện thoại người dùng nhập vào đúng hay không
     * 
     * @param field        chứa trường số điện thoại người dùng nhập vào
     * @param sb           đối tượng StringBuilder để append thông điệp lỗi
     * @param errorMessage chứa thông điệp lỗi
     */
    public static void validateSoDT(TextField field, StringBuilder sb, String errorMessage) {
        String regex = "\\d{9,10}";
        if (!field.getText().matches(regex)) {
            sb.append(errorMessage).append("\n");
            field.requestFocus();
            field.selectAll();
        }
    }

    /**
     * kiểm tra trường năm sinh người dùng nhập vào đúng hay không
     * 
     * @param field        chứa trường năm sinh người dùng nhập vào
     * @param sb           đối tượng StringBuilder để append thông điệp lỗi
     * @param errorMessage chứa thông điệp lỗi
     */
    public static void validateNamSinh(TextField field, StringBuilder sb, String errorMessage) {
        String regex = "\\d{4}";
        if (!field.getText().matches(regex)) {
            sb.append(errorMessage).append("\n");
            field.requestFocus();
            field.selectAll();
        }
    }

    public static void validateTenDN(TextField field, StringBuilder sb, String errorMessage) {
        String regex = "[a-zA-Z0-9]+";
        if (!field.getText().matches(regex)) {
            sb.append(errorMessage).append("\n");
            field.requestFocus();
            field.selectAll();
        }
    }

    /**
     * Giải thích: ^ # bắt đầu chuỗi (?=.*[0-9]) # Chữ số phải xuất hiện ít nhất 1 lần (?=.*[a-z]) # Chữ cái thường phải xuất hiện ít nhất 1 lần (?=.*[A-Z]) # Chữ cái thường phải xuất hiện ít nhất 1 lần (?=.*[@#$%^&+=]) # Một kí tự đặt biệt phải xuất hiện ít nhất 1 lần (?=\\S+$) # Không có khoảng trắng .{8,} # Tối đa 8 kí tự $ # Kết thúc chuỗi
     * 
     * @param field        mật khẩu người dùng nhập vào
     * @param sb
     * @param errorMessage
     */
    public static void validateMatKhau(TextField field, StringBuilder sb, String errorMessage) {
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        if (!field.getText().matches(regex)) {
            sb.append(errorMessage).append("\n");
            field.requestFocus();
            field.selectAll();
        }
    }

    /**
     * kiểm tra trường đơn giá người dùng nhập vào
     * 
     * @param field        chứa trường đơn giá người dùng nhập vào
     * @param sb           đối tượng StringBuilder để append thông điệp lỗi
     * @param errorMessage chứa thông điệp lỗi
     */
    public static void validateDonGia(TextField field, StringBuilder sb, String errorMessage) {
        String regex = "[0-9]+";
        if (!field.getText().matches(regex)) {
            sb.append(errorMessage).append("\n");
            field.requestFocus();
            field.selectAll();
        }
    }

    /**
     * kiểm tra trường số điện thoại người dùng nhập vào
     * 
     * @param field        chứa trường đơn giá người dùng nhập vào
     * @param sb           đối tượng StringBuilder để append thông điệp lỗi
     * @param errorMessage chứa thông điệp lỗi
     */
    public static void validateKhongTrungSDT(TextField field, StringBuilder sb, String errorMessage) {
        KhachHangDAO khachHangDAO = new KhachHangDAO();
        List<KhachHang> dsKhachHang = khachHangDAO.getDanhSachKhachHang();
        for (KhachHang khachHang : dsKhachHang) {
            if (field.getText().equals(khachHang.getSoDienThoai())) {
                sb.append(errorMessage).append("\n");
                field.requestFocus();
                field.selectAll();
            }
        }
    }

    public static void validateSoLuongSanPham(TextField field, StringBuilder sb, String errorMessage) {
        String regex = "[0-9]+";
        int soLuong;
        try {
            soLuong = Integer.parseInt(field.getText());
        } catch (NumberFormatException e) {
            sb.append(errorMessage).append("\n");
            field.requestFocus();
            field.selectAll();
            return;
        }

        if (!field.getText().matches(regex) || soLuong <= 0) {
            sb.append(errorMessage).append("\n");
            field.requestFocus();
            field.selectAll();
        }
    }
}
