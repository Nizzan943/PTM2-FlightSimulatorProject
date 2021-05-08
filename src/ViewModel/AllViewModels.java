package ViewModel;

import java.util.Observable;
import java.util.Observer;

public abstract class AllViewModels extends Observable implements Observer {
    @Override
    public abstract void update(Observable o, Object arg);
}
