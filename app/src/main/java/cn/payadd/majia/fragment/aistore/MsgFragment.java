package cn.payadd.majia.fragment.aistore;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.fdsj.credittreasure.R;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupCacheInfo;
import com.tencent.imsdk.ext.sns.TIMFriendFutureItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.payadd.majia.activity.aistore.AIStoreMainActivity;
import cn.payadd.majia.adapter.aistore.ConversationAdapter;
import cn.payadd.majia.entity.aistore.Conversation;
import cn.payadd.majia.entity.aistore.CustomMessage;
import cn.payadd.majia.entity.aistore.FriendshipConversation;
import cn.payadd.majia.entity.aistore.MessageFactory;
import cn.payadd.majia.entity.aistore.NomalConversation;
import cn.payadd.majia.im.presentation.presenter.FriendshipManagerPresenter;
import cn.payadd.majia.im.presentation.viewfeatures.ConversationView;
import cn.payadd.majia.im.presentation.viewfeatures.FriendshipMessageView;
import cn.payadd.majia.presenter.aistore.ConversationPresenter;
import cn.payadd.majia.util.aistore.PushUtil;

/**
 * Created by lang on 2018/6/15.
 */

public class MsgFragment extends Fragment implements ConversationView, FriendshipMessageView {

    private ListView listView;

    private View view;
    private List<Conversation> conversationList = new LinkedList<>();
    private ConversationAdapter adapter;
    private ConversationPresenter presenter;
    private FriendshipManagerPresenter friendshipManagerPresenter;
    private List<String> groupList;
    private FriendshipConversation friendshipConversation;
    private List<Map<String, String>> nameAndAvatarList;
    private Map<String, String> map;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null){
            view = inflater.inflate(R.layout.fragment_msg, container, false);
            listView = (ListView) view.findViewById(R.id.listView);
            adapter = new ConversationAdapter(getActivity(), R.layout.item_conversation, conversationList);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    conversationList.get(position).navToDetail(getActivity());
                }
            });
            friendshipManagerPresenter = new FriendshipManagerPresenter(this);
            presenter = new ConversationPresenter(this);
            presenter.getConversation();
            registerForContextMenu(listView);
        }
        adapter.notifyDataSetChanged();
        return view;

    }

    @Override
    public void initView(List<TIMConversation> conversationList) {
        this.conversationList.clear();
        groupList = new ArrayList<>();
        for (TIMConversation item : conversationList){
            switch (item.getType()){
                case C2C:
                case Group:
                    this.conversationList.add(new NomalConversation(item));
                    groupList.add(item.getPeer());
                    break;
            }
        }
        friendshipManagerPresenter.getFriendshipLastMessage();
    }

    private void getNameAndAvatar() {
        if (nameAndAvatarList == null) {
            nameAndAvatarList = new ArrayList<>();
        }
        List<String> list = new ArrayList<>();
        for (Conversation c : conversationList) {
            list.add(c.getIdentify());
        }
        TIMFriendshipManager.getInstance().getUsersProfile(list, new TIMValueCallBack<List<TIMUserProfile>>() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess(List<TIMUserProfile> timUserProfiles) {
                nameAndAvatarList.clear();
                for (TIMUserProfile timUserProfile : timUserProfiles) {
                    map = new HashMap<>();
                    map.put("name", timUserProfile.getNickName());
                    map.put("avatar", timUserProfile.getFaceUrl());
                    map.put("identify", timUserProfile.getIdentifier());
                    nameAndAvatarList.add(map);
                }

                adapter.setNameAndAvatar(nameAndAvatarList);
            }
        });
    }

    @Override
    public void updateMessage(TIMMessage message) {
        if (message == null){
            adapter.notifyDataSetChanged();
            return;
        }

        if (MessageFactory.getMessage(message) instanceof CustomMessage) return;
        NomalConversation conversation = new NomalConversation(message.getConversation());
        Iterator<Conversation> iterator = conversationList.iterator();
        while (iterator.hasNext()){
            Conversation c = iterator.next();
            if (conversation.equals(c)){
                conversation = (NomalConversation) c;
                iterator.remove();
                break;
            }
        }
        conversation.setLastMessage(MessageFactory.getMessage(message));
        conversationList.add(conversation);
        refresh();
    }

    @Override
    public void updateFriendshipMessage() {
        friendshipManagerPresenter.getFriendshipLastMessage();
    }

    @Override
    public void removeConversation(String identify) {
        Iterator<Conversation> iterator = conversationList.iterator();
        while(iterator.hasNext()){
            Conversation conversation = iterator.next();
            if (conversation.getIdentify()!=null&&conversation.getIdentify().equals(identify)){
                iterator.remove();
                adapter.notifyDataSetChanged();
                return;
            }
        }
    }

    @Override
    public void updateGroupInfo(TIMGroupCacheInfo info) {
        for (Conversation conversation : conversationList){
            if (conversation.getIdentify() != null && conversation.getIdentify().equals(info.getGroupInfo().getGroupId())) {
                adapter.notifyDataSetChanged();
                return;
            }
        }
    }

    @Override
    public void refresh() {
        Collections.sort(conversationList);
        getNameAndAvatar();
        adapter.notifyDataSetChanged();
        if (getActivity() instanceof AIStoreMainActivity)
            ((AIStoreMainActivity) getActivity()).setMsgUnread(getTotalUnreadNum());
    }

    @Override
    public void onGetFriendshipLastMessage(TIMFriendFutureItem message, long unreadCount) {
        if (friendshipConversation == null){
            friendshipConversation = new FriendshipConversation(message);
            conversationList.add(friendshipConversation);
        }else{
            friendshipConversation.setLastMessage(message);
        }
        friendshipConversation.setUnreadCount(unreadCount);
        Collections.sort(conversationList);
        refresh();
    }

    @Override
    public void onGetFriendshipMessage(List<TIMFriendFutureItem> message) {
        friendshipManagerPresenter.getFriendshipLastMessage();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        NomalConversation conversation = (NomalConversation) conversationList.get(info.position);
        switch (item.getItemId()) {
            case 1:
                if (conversation != null){
                    if (presenter.delConversation(conversation.getType(), conversation.getIdentify())){
                        conversationList.remove(conversation);
                        adapter.notifyDataSetChanged();
                    }
                }
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
        PushUtil.getInstance().reset();
    }

    private long getTotalUnreadNum(){
        long num = 0;
        for (Conversation conversation : conversationList){
            num += conversation.getUnreadNum();
        }
        return num;
    }
}