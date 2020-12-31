package android.app;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;

import java.util.List;

/**
 * @author Omico 2020/12/16
 */
public interface IActivityTaskManager extends IInterface {

    List<ActivityManager.RunningTaskInfo> getTasks(int maxNum);

    abstract class Stub extends Binder implements IActivityTaskManager {

        public static IActivityTaskManager asInterface(IBinder obj) {
            throw new UnsupportedOperationException();
        }
    }
}
