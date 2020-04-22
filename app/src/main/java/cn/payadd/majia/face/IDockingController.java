package cn.payadd.majia.face;

/**
 * Created by df on 2017/9/25.
 */

public interface IDockingController {

        int DOCKING_HEADER_HIDDEN = 1;
        int DOCKING_HEADER_DOCKING = 2;
        int DOCKING_HEADER_DOCKED = 3;

        int getDockingState(int firstVisibleGroup, int firstVisibleChild);

}
