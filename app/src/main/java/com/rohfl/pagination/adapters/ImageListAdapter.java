package com.rohfl.pagination.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rohfl.pagination.R;
import com.rohfl.pagination.listeners.OnClickListener;
import com.rohfl.pagination.listeners.OnLastItemScrolledListener;
import com.rohfl.pagination.models.ResponseGetImageListItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.MainViewHolder> {

    Context mContext;
    List<ResponseGetImageListItem> mList;
    OnLastItemScrolledListener onLastItemScrolledListener;
    OnClickListener onClickListener;

    public ImageListAdapter(Context mContext) {
        this.mContext = mContext;
        this.mList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if (viewType == 0) {
            v = LayoutInflater.from(mContext).inflate(R.layout.item_image_data, parent, false);
            return new ViewHolder(v);
        } else {
            v = LayoutInflater.from(mContext).inflate(R.layout.item_list_loading, parent, false);
            return new ProgressView(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        // if the we are binding the last position then we load more data
        if (position == mList.size() - 1 && mList.get(position) == null) {
            onLastItemScrolledListener.onLastItemScrolled();
        } else {
            // if the holder is an object/instance of ViewHolder then do the following else do nothing
            if (holder instanceof ViewHolder) {
                ResponseGetImageListItem item = mList.get(position);

                ((ViewHolder) holder).txtImageTitle.setText(mList.get(position).getTitle());

                if (item.getThumbnailUrl() != null) {
                    // removing glide because it is unable to load placeholder.com images properly
//                    Glide.with(mContext).load(item.getThumbnailUrl() + ".jpg").error(R.mipmap.error).into(((ViewHolder) holder).imgImage);
                    Picasso.get()
                            .load(item.getThumbnailUrl())
                            .into(((ViewHolder) holder).imgImage);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mList.get(position) != null)
            return 0;
        else
            return 1;
    }

    /**
     * adds the data in the list
     * @param mList the images list
     */
    public void updateList(List<ResponseGetImageListItem> mList) {
        int oldSize = this.mList.size();
        this.mList.addAll(mList);
        notifyItemRangeChanged(oldSize, this.mList.size() - 1);
    }

    /**
     * deletes the item from the list
     * @param position the position of the item we want to remove
     */
    public void deleteItem(int position) {
        this.mList.remove(position);
        if(mList.size() == 0) {
            Toast.makeText(mContext, "All items deleted.", Toast.LENGTH_SHORT).show();
        }
        notifyItemRemoved(position);
    }

    /**
     * removes the null from the list, hence the progress bar is removed from the view
     */
    public void removeNull() {
        if (this.mList.size() > 0 && this.mList.get(this.mList.size() - 1) == null) {
            this.mList.remove(this.mList.size() - 1);
            notifyItemRemoved(this.mList.size());
        }
    }

    /**
     * creating a callback to detect the last object is reached or not
     * @param onLastItemScrolledListener the Listener for the last item scrolled
     */
    public void setOnLastItemScrolledListener(OnLastItemScrolledListener onLastItemScrolledListener) {
        this.onLastItemScrolledListener = onLastItemScrolledListener;
    }

    /**
     * creating a callback to perform the click event
     * @param onClickListener the Listener for the click
     */
    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {
        public MainViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public class ViewHolder extends MainViewHolder {
        TextView txtImageTitle;
        ImageView imgImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtImageTitle = itemView.findViewById(R.id.txt_image_title);
            imgImage = itemView.findViewById(R.id.img_image);

            itemView.setOnClickListener(v -> onClickListener.onItemClicked(getAdapterPosition()));
        }
    }

    public static class ProgressView extends MainViewHolder {
        public ProgressView(@NonNull View itemView) {
            super(itemView);
        }
    }

}
