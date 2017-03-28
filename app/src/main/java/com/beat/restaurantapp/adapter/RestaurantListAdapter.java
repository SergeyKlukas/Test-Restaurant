package com.beat.restaurantapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.beat.restaurantapp.R;
import com.beat.restaurantapp.models.Restaurant;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by beat on 3/26/2017.
 */

public class RestaurantListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Restaurant> restaurants = new ArrayList<>();

    public RestaurantListAdapter(Context context, ArrayList<Restaurant> restaurants) {
        this.context = context;
        this.restaurants = restaurants;
    }

    @Override
    public int getCount() {
        if (this.restaurants != null)
            return this.restaurants.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (this.restaurants != null && this.restaurants.size() > 0)
            return this.restaurants.get(position);
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_restaurant, parent, false);
            holder = new ViewHolder();

            holder.imgThumb = (ImageView) convertView.findViewById(R.id.restaurant_pic);
            holder.txtName = (TextView)  convertView.findViewById(R.id.restaurant_name);
            holder.txtCategory = (TextView) convertView.findViewById(R.id.restaurant_category);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtName.setText(restaurants.get(position).getStrName());
        holder.txtCategory.setText(restaurants.get(position).getStrCategory());

        Bitmap imageBitmap = null;
        try {
            imageBitmap = decodeFromFirebaseBase64(restaurants.get(position).getPicture());
            holder.imgThumb.setImageBitmap(imageBitmap);
        } catch (IOException e) {
        }

        return convertView;
    }

    private Bitmap decodeFromFirebaseBase64(String image) throws IOException {
        byte[] decodedByteArray = android.util.Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
    }

    public void setData(ArrayList<Restaurant> restaurants) {
        this.restaurants = restaurants;
        notifyDataSetChanged();
    }

    private class ViewHolder {
        private ImageView imgThumb;
        private TextView txtName;
        private TextView txtCategory;
    }
}
