package com.gmail.white.myapplication.callbacks;

import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import com.gmail.white.myapplication.R;

public abstract class MainActionModeCallback implements ActionMode.Callback {
    private ActionMode actionMode;
    private MenuItem countItem;
    private MenuItem shareItem;
    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.main_action_menu,menu);
        this.actionMode = actionMode;
        this.countItem = menu.findItem(R.id.action_checked_count);
        this.shareItem = menu.findItem(R.id.action_share_notes);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {

    }

    public ActionMode getActionMode() {
        return actionMode;
    }

    public void setCountItem(String checkedCount) {
        if (countItem!=null)
        this.countItem.setTitle(checkedCount);
    }

    public void shareItemVisibility(boolean b) {
        shareItem.setVisible(b);
    }
}
