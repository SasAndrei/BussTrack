package Utils;

import com.example.busstrack.MainActivity;

import java.util.ArrayList;

public class CustomArrayList<T> extends ArrayList<T> {
    private final MainActivity instance;
    private ArrayList<T> list;

    public CustomArrayList() {
        list = new ArrayList();
        instance = null;
    }
    public CustomArrayList(MainActivity instance) {
        list = new ArrayList();
        this.instance = instance;
    }

    public boolean add(T elementToAdd) {
        boolean value = list.add(elementToAdd);

        instance.checkCurrentUser();


        return value;
    }

    public boolean remove(Object elementToRemove) {
        boolean value = list.remove(elementToRemove);

        //instance.DoByEachRemove();

        return value;
    }

    public ArrayList<T> getList() {
        return list;
    }

}