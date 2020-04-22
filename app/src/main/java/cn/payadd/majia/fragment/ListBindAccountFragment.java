package cn.payadd.majia.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.fdsj.credittreasure.R;
import com.utils.Utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.payadd.majia.activity.UpdateAccountActivity;
import cn.payadd.majia.adapter.BindAccountItemAdapter;
import cn.payadd.majia.face.IFragment;
import cn.payadd.majia.presenter.FundAuthPresenter;

/**
 * Created by df on 2017/6/20.
 */

public class ListBindAccountFragment extends Fragment implements IFragment {
    private ListView listView;
    private FundAuthPresenter fundAuthPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_list_bind_acc,
                container, false);
        listView = (ListView) layout.findViewById(R.id.lv_bind_acc);
        fundAuthPresenter = new FundAuthPresenter(getActivity(), this);
        return layout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        String username = Utilities.getUsernameForLogin(getActivity());
        fundAuthPresenter.queryBindInfo(username);

    }

    @Override
    public void updateModel(String key, Object data) {
        switch (key) {
            case "bindInfo":
                Map<String, String> respData = (Map<String, String>) data;
                String respCode = respData.get("respCode");
                String respDesc = respData.get("respDesc");
                if ("000000".equals(respCode)) {
                    String alipayLogonId = respData.get("alipayLogonId");
                    List<Map<String, String>> list = new ArrayList<>();
                    Map<String, String> map = new HashMap<>();
                    map.put("accountNo", alipayLogonId);
                    list.add(map);
                    listView.setAdapter(new BindAccountItemAdapter(getActivity(), list));
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            switch (position) {
                                case 0:
                                    Intent intent = new Intent(getActivity(), UpdateAccountActivity.class);
                                    startActivity(intent);
                                    break;
                                default:
                                    break;
                            }
                        }
                    });
                }


                break;
        }

    }
}
