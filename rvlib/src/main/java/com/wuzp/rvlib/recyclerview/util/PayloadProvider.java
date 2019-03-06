package com.wuzp.rvlib.recyclerview.util;

/**
 * A Interface class used by DiffUtil while calculating the diff between two lists.
 *
 * @param <M> Refers to the model class
 */
public interface PayloadProvider<M> {

    /**
     * Called by the DataManager when it wants to check whether two items have the same data.
     * DataManager uses this information to detect if the contents of an item has changed.
     * <p>
     * DataManager uses this method to check equality instead of {@link Object#equals(Object)}
     * so that you can change its behavior depending on your UI.
     * </p>
     *
     * @param oldItem The item in the old list
     * @param newItem The item in the new list which replaces the oldItem
     * @return True if the contents of the items are the same or false if they are different, ie.,
     * you should return whether the items' visual representations are the same.
     */
    boolean areContentsTheSame(M oldItem, M newItem);

    /**
     * Called by the DataManager when it wants to get the payload of changed elements.
     * <p>
     * For example, if you are using DiffUtil with {@link RecyclerView}, you can return the
     * particular field that changed in the item and your
     * {@link RecyclerView.ItemAnimator ItemAnimator} can use that
     * information to run the correct animation.
     * </p>
     * Default implementation returns {@code null}.
     *
     * @param oldItem The item in the old list
     * @param newItem The item in the new list
     * @return A payload object that represents the change between the two items.
     */
    @SuppressWarnings("UnusedParameters") Object getChangePayload(M oldItem, M newItem);
}
