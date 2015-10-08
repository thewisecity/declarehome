package com.wisecityllc.cookedapp.adapters.delegates;

import com.wisecityllc.cookedapp.parseClasses.Group;

/**
 * Created by dexterlohnes on 10/5/15.
 */
public interface GroupsCheckboxAdapterDelegate {

    void groupChecked(Group group);
    void groupUnchecked(Group group);
    void groupsFinished();
    boolean isGroupChecked(Group group);

}
