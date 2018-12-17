package app.com.forheypanel.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import com.pnikosis.materialishprogress.ProgressWheel;
import app.com.forheypanel.R;
import butterknife.Bind;

/**
 * Created by nayram on 5/26/16.
 */
public class Trans_Order_List extends BaseActivity {
    @Bind(R.id.progress_wheel)
    ProgressWheel progressWheel;
    @Bind(R.id.rvOrders)
    RecyclerView rvOrders;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerview_layout);

    }
}
