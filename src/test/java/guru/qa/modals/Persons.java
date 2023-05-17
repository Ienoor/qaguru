package guru.qa.modals;

import com.google.gson.annotations.SerializedName;

public class Persons {
    public String name;
    public int age;
    public Boolean car;
    public Jobs jobs;

    public static class Jobs {
        public String[] developer;
    }
}
