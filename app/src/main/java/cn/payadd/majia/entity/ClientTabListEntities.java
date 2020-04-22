package cn.payadd.majia.entity;

import com.flyco.tablayout.listener.CustomTabEntity;

public class ClientTabListEntities implements CustomTabEntity {
    public String tabTitle;
    public int selectedIcon;
    public int unSelectedIcon;

    public ClientTabListEntities(String tabTitle, int selectedIcon, int unSelectedIcon) {
        this.tabTitle = tabTitle;
        this.selectedIcon = selectedIcon;
        this.unSelectedIcon = unSelectedIcon;
    }

    @Override
    public String getTabTitle() {
        return tabTitle;
    }

    @Override
    public int getTabSelectedIcon() {
        return selectedIcon;
    }

    @Override
    public int getTabUnselectedIcon() {
        return unSelectedIcon;
    }
}
