package edu.neu.madcourse.dankreymer.multiplayer;

import edu.neu.madcourse.dankreymer.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DabbleMRealTime extends Fragment{

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dabble_m_realtime, container, false);
    }
}
