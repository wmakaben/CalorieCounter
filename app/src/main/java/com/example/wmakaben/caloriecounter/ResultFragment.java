package com.example.wmakaben.caloriecounter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ResultFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResultFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    private String json;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param json Parameter 1.
     * @return A new instance of fragment ResultFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ResultFragment newInstance(String json) {
        ResultFragment fragment = new ResultFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, json);
        fragment.setArguments(args);
        return fragment;
    }

    public ResultFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            json = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_result, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    private void getIngredients(){
        ArrayList<Ingredient> ingredientList = new ArrayList<Ingredient>();
        Ingredient ingredient = new Ingredient("Bread", 300, true);
        ingredientList.add(ingredient);
        ingredient = new Ingredient("Bread", 300, true);
        ingredientList.add(ingredient);
        ingredient = new Ingredient("Ham", 200, true);
        ingredientList.add(ingredient);
        ingredient = new Ingredient("Cheese", 300, true);
        ingredientList.add(ingredient);
        ingredient = new Ingredient("Lettuce", 20, true);
        ingredientList.add(ingredient);
        ingredient = new Ingredient("Tomatoes", 40, true);
        ingredientList.add(ingredient);
        ingredient = new Ingredient("Onions", 30, true);
        ingredientList.add(ingredient);
    }

    private class IngredientCustomAdapter extends ArrayAdapter<Ingredient>{
        private ArrayList<Ingredient> ingredientList;

        public IngredientCustomAdapter(Context context, int textViewResourceId, ArrayList<Ingredient> ingredientList){
            super(context, textViewResourceId, ingredientList);
            this.ingredientList = new ArrayList<Ingredient>();
            this.ingredientList.addAll(ingredientList);
        }

        private class ViewHolder {
            CheckBox name;
            TextView cal;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            if(convertView == null){
                LayoutInflater vi = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.history_list_item, null);

                holder = new ViewHolder();
                holder.name = (CheckBox)convertView.findViewById(R.id.ingredient_checkbox);
                holder.cal = (TextView)convertView.findViewById(R.id.ingredient_calories);
                convertView.setTag(holder);

                holder.name.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v){
                        CheckBox cb = (CheckBox)v;
                        Ingredient ingredient = (Ingredient)cb.getTag();
                        // Add/Subtract calories from total
                    }
                });
            }
            else{
                holder = (ViewHolder) convertView.getTag();
            }

            Ingredient ingredient = ingredientList.get(position);
            holder.name.setChecked(ingredient.isSelected());
            holder.name.setText(ingredient.getName());
            holder.cal.setText(ingredient.getCalories());

            return convertView;
        }
    }
    

}
