package com.hftapps.mobileinformation;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class RequestListAdapter extends ArrayAdapter<Requests> {

    Context context;

    public RequestListAdapter(Context cxt, int resource, List<Requests> objects) {
        super(cxt, resource, objects);
        context = cxt;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View itemView = convertView;
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if(itemView == null)
            itemView = mInflater.inflate(R.layout.list_item,parent,false);

        Requests request = getItem(position);

        itemView.setTag(request.get_id());



        TextView mobile = (TextView) itemView.findViewById(R.id.requestNumber);
        mobile.setText(request.get_mobile());

        TextView op = (TextView) itemView.findViewById(R.id.requestOperatorType);
        op.setText(request.get_op_name());

        TextView cr = (TextView) itemView.findViewById(R.id.requestCircleType);
        cr.setText(request.get_cr_name());

        TextView sim = (TextView) itemView.findViewById(R.id.requestSimType);
        sim.setText(request.get_sim_type());

        ImageView imageView = (ImageView) itemView.findViewById(R.id.requestlogo);

        String uri = "@drawable/" + request.get_logo();
        int imageResource =  context.getResources().getIdentifier(uri, null, context.getPackageName());
        imageView.setImageResource(imageResource);


        return itemView;
    }
}

