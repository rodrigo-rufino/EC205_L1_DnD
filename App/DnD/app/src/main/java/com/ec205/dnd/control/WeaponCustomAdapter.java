package com.ec205.dnd.control;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ec205.dnd.R;
import com.ec205.dnd.model.Weapon;

import java.util.ArrayList;

/**
 * Created by rodri on 15-Apr-17.
 */

public class WeaponCustomAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<Weapon> objects;

    private class ViewHolder {
        TextView weaponTextView;
    }

    public WeaponCustomAdapter(Context context, ArrayList<Weapon> objects) {
        inflater = LayoutInflater.from(context);
        this.objects = objects;
    }

    public int getCount() {
        return objects.size();
    }

    public Weapon getItem(int position) {
        return objects.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.adapter_list_view, null);
            holder.weaponTextView = (TextView) convertView.findViewById(R.id.user_text_view);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.weaponTextView.setText(objects.get(position).getName());
        return convertView;
    }
}