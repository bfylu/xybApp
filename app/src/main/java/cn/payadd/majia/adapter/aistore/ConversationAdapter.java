package cn.payadd.majia.adapter.aistore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fdsj.credittreasure.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.payadd.majia.entity.aistore.Conversation;
import cn.payadd.majia.util.StringUtil;
import cn.payadd.majia.util.TimeUtil;
import cn.payadd.majia.view.CircleImageView;

/**
 * 会话界面adapter
 */
public class ConversationAdapter extends ArrayAdapter<Conversation> {

    private int resourceId;
    private ViewHolder viewHolder;
    private List<Map<String, String>> nameAndAvatarList;

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param objects  The objects to represent in the ListView.
     */
    public ConversationAdapter(Context context, int resource, List<Conversation> objects) {
        super(context, resource, objects);
        resourceId = resource;
        if (nameAndAvatarList == null) {
            nameAndAvatarList = new ArrayList<>();
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView != null){
            viewHolder = (ViewHolder) convertView.getTag();
        }else{
            convertView = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.avatar = (CircleImageView) convertView.findViewById(R.id.avatar);
            viewHolder.lastMessage = (TextView) convertView.findViewById(R.id.last_message);
            viewHolder.time = (TextView) convertView.findViewById(R.id.message_time);
            viewHolder.unread = (TextView) convertView.findViewById(R.id.unread_num);
            convertView.setTag(viewHolder);
        }
        final Conversation data = getItem(position);

        String name = "", avatar = "";
        for (Map<String, String> map : nameAndAvatarList) {
            if (StringUtil.equals(data.getIdentify(), map.get("identify"))) {
                name = map.get("name");
                avatar = map.get("avatar");
            }
        }

        if (StringUtil.isNotEmpty(name)) {
            viewHolder.tvName.setText(name);
        } else {
            viewHolder.tvName.setText(data.getIdentify());
        }

        if(StringUtil.isNotEmpty(avatar)) {
            ImageLoader.getInstance().displayImage(avatar, viewHolder.avatar);
        } else {
            viewHolder.avatar.setImageResource(data.getAvatar());
        }


        viewHolder.lastMessage.setText(data.getLastMessageSummary());
        viewHolder.time.setText(TimeUtil.getTimeStr(data.getLastMessageTime()));
        long unRead = data.getUnreadNum();
        if (unRead <= 0){
            viewHolder.unread.setVisibility(View.INVISIBLE);
        }else{
            viewHolder.unread.setVisibility(View.VISIBLE);
            String unReadStr = String.valueOf(unRead);
            if (unRead < 10){
                viewHolder.unread.setBackground(getContext().getResources().getDrawable(R.mipmap.point1));
            }else {
                viewHolder.unread.setBackground(getContext().getResources().getDrawable(R.mipmap.point2));
                if (unRead > 99){
                    unReadStr = getContext().getResources().getString(R.string.time_more);
                }
            }
            viewHolder.unread.setText(unReadStr);
        }
        return convertView;
    }

    public void setNameAndAvatar(List<Map<String, String>> nameAndAvatarList) {
        this.nameAndAvatarList = nameAndAvatarList;
        notifyDataSetChanged();
    }

    public class ViewHolder{
        public TextView tvName;
        public CircleImageView avatar;
        public TextView lastMessage;
        public TextView time;
        public TextView unread;

    }
}
