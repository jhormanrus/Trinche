package com.trinche.app.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.awesome.dialog.AwesomeSwitchableDialog;
import com.loicteillard.easytabs.EasyTabs;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.trinche.app.R;
import com.trinche.app.adapters.TABhome;

import org.jetbrains.annotations.NotNull;

public class LOhome extends Fragment implements View.OnClickListener, MaterialSearchBar.OnSearchActionListener, PopupMenu.OnMenuItemClickListener {

    FloatingActionButton searchFAB;
    EasyTabs easyTabs;
    ViewPager viewPager;
    MaterialSearchBar search_userMSB, search_recipeMSB;

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_home, container, false);

        init(v);

        easyTabs.setPagerListener(new EasyTabs.PagerListener() {
            @Override
            public void onTabSelected(int position) {
            }
        });
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.searchFAB:
                new AwesomeSwitchableDialog(getActivity())
                        .setTopView(R.layout.tiny_searchuser)
                        .setBottomView(R.layout.tiny_searchrecipe)
                        .configureTopView(new AwesomeSwitchableDialog.topViewConfigurator() {
                            @Override
                            public void configureViewTop(@NotNull View view) {
                                search_userMSB = (MaterialSearchBar) view.findViewById(R.id.search_userMSB);
                                search_userMSB.setOnSearchActionListener(LOhome.this);
                                search_userMSB.inflateMenu(R.menu.search_user);
                                search_userMSB.getMenu().setOnMenuItemClickListener(LOhome.this);
                                search_userMSB.addTextChangeListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                        System.out.println("before");
                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                        System.out.println("text: " + search_userMSB.getText());
                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {
                                        System.out.println("after");
                                    }
                                });
                                /*LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                RVresponseAdapter adapter = new RVresponseAdapter(inflater);

                                List<Usuario> suggestions = new ArrayList<>();
                                for (int i = 1; i < 11; i++) {
                                    suggestions.add(new Usuario("Corki" + i));
                                    System.out.println(i);
                                }

                                adapter.setSuggestions(suggestions);
                                search_userMSB.setCustomSuggestionAdapter(adapter);*/
                            }
                        }).configureBottomView(new AwesomeSwitchableDialog.bottomViewConfigurator() {
                            @Override
                            public void configureViewBottom(@NotNull View view) {
                                search_recipeMSB = (MaterialSearchBar) view.findViewById(R.id.search_recipeMSB);
                                search_recipeMSB.setOnSearchActionListener(LOhome.this);
                                search_recipeMSB.inflateMenu(R.menu.search_recipe);
                                search_recipeMSB.getMenu().setOnMenuItemClickListener(LOhome.this);
                            }
                        }).setTopViewColor(Color.parseColor("#FFB475")).setBottomViewColor(Color.parseColor("#FFFFFF"))
                        .setTopViewIcon(R.drawable.ic_twotone_account_circle_24px, Color.parseColor("#FFFFFF"))
                        .setBottomViewIcon(R.drawable.ic_twotone_receipt_24px, Color.parseColor("#000000")).show();
                break;
        }
    }

    @Override
    public void onSearchStateChanged(boolean enabled) {
        String s = enabled ? "enabled" : "disabled";
        System.out.println(s);
    }

    @Override
    public void onSearchConfirmed(CharSequence text) {
        System.out.println(text.toString());
    }

    @Override
    public void onButtonClicked(int buttonCode) {
        switch (buttonCode) {
            case MaterialSearchBar.BUTTON_BACK:
                break;
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nom_apSM:
                System.out.println("nom_ap");
                break;

            case R.id.usuarioSM:
                System.out.println("usuario");
                break;

            case R.id.correoSM:
                System.out.println("correo");
                break;

            case R.id.nom_reSM:
                System.out.println("nom_re");
                break;

            case R.id.creatorSM:
                System.out.println("creator");
                break;
        }
        return true;
    }

    private void init(View v) {
        easyTabs = v.findViewById(R.id.easytabs);
        viewPager = v.findViewById(R.id.viewpager);
        TABhome pagerAdapter = new TABhome(getFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        easyTabs.setViewPager(viewPager, 0);

        searchFAB = (FloatingActionButton) v.findViewById(R.id.searchFAB);
        searchFAB.setOnClickListener(this);
    }
}
