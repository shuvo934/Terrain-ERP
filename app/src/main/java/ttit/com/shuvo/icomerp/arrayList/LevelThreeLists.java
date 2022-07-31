package ttit.com.shuvo.icomerp.arrayList;

public class LevelThreeLists {
    private String id;
    private String code;
    private String sh_code;
    private String name;

    public LevelThreeLists(String id, String code, String sh_code, String name) {
        this.id = id;
        this.code = code;
        this.sh_code = sh_code;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSh_code() {
        return sh_code;
    }

    public void setSh_code(String sh_code) {
        this.sh_code = sh_code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
