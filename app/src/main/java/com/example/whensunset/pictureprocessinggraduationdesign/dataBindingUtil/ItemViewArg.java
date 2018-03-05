package com.example.whensunset.pictureprocessinggraduationdesign.dataBindingUtil;

import android.support.annotation.LayoutRes;

/**
 * Created by Administrator on 2016/12/7 0007.
 */
public class ItemViewArg<T> {
    public static <T> ItemViewArg<T> of(ItemView itemView) {
        return new ItemViewArg<>(itemView);
    }

    public static <T> ItemViewArg<T> of(ItemViewSelector<T> selector) {
        return new ItemViewArg<>(selector);
    }

    private final ItemView itemView;
    private final ItemViewSelector<T> selector;

    private ItemViewArg(ItemView itemView) {
        this.itemView = itemView;
        this.selector = new ItemViewSelector<T>() {
            @Override
            public void select(ItemView itemView, int position, T item) {

            }

            @Override
            public int viewTypeCount() {
                return 1;
            }
        };
    }

    private ItemViewArg(ItemViewSelector<T> selector) {
        this.itemView = new ItemView();
        this.selector = selector;
    }

    public void select(int position, T item) {
        selector.select(itemView, position, item);
    }

    public int bindingVariable() {
        return itemView.bindingVariable();
    }

    public int layoutRes() {
        return itemView.layoutRes();
    }

    public int viewTypeCount() {
        return selector.viewTypeCount();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemViewArg<?> that = (ItemViewArg<?>) o;

        if (!itemView.equals(that.itemView)) return false;
        return selector == that.selector;
    }

    @Override
    public int hashCode() {
        int result = itemView.hashCode();
        result = 31 * result + selector.hashCode();
        return result;
    }

    public interface ItemViewSelector<T> {

        void select(ItemView itemView, int position, T item);


        int viewTypeCount();
    }

    public abstract static class ItemViewSelectorDefault<T> implements ItemViewSelector<T> {

        @Override
        public int viewTypeCount() {
            return 1;
        }
    }

    public static class ItemView {

        public static final int BINDING_VARIABLE_NONE = 0;

        private int bindingVariable;
        @LayoutRes
        private int layoutRes;


        public static ItemView of(int bindingVariable, @LayoutRes int layoutRes) {
            return new ItemView().setBindingVariable(bindingVariable).setLayoutRes(layoutRes);
        }


        public ItemView set(int bindingVariable, @LayoutRes int layoutRes) {
            this.bindingVariable = bindingVariable;
            this.layoutRes = layoutRes;
            return this;
        }


        public ItemView setBindingVariable(int bindingVariable) {
            this.bindingVariable = bindingVariable;
            return this;
        }


        public ItemView setLayoutRes(@LayoutRes int layoutRes) {
            this.layoutRes = layoutRes;
            return this;
        }

        public int bindingVariable() {
            return bindingVariable;
        }

        @LayoutRes
        public int layoutRes() {
            return layoutRes;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ItemView itemView = (ItemView) o;

            if (bindingVariable != itemView.bindingVariable) return false;
            return layoutRes == itemView.layoutRes;
        }

        @Override
        public int hashCode() {
            int result = bindingVariable;
            result = 31 * result + layoutRes;
            return result;
        }
    }

}
