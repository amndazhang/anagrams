public class Word {
    private String str;
    public Word (String str){
        this.str = str;
    }

    public int hashCode(){
        int code = 0;
        for(int i = 0; i < str.length(); i++){
            code += ((str.charAt(i) - 96)*(26^i));
        }
        return code;
    }

    public String toString(){
        return str;
    }
}