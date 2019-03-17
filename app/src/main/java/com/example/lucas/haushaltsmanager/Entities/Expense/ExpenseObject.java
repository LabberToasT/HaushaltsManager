package com.example.lucas.haushaltsmanager.Entities.Expense;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.lucas.haushaltsmanager.App.app;
import com.example.lucas.haushaltsmanager.Entities.Account;
import com.example.lucas.haushaltsmanager.Entities.Category;
import com.example.lucas.haushaltsmanager.Entities.Currency;
import com.example.lucas.haushaltsmanager.Entities.Tag;
import com.example.lucas.haushaltsmanager.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ExpenseObject implements Parcelable, Booking {
    /**
     * regenerating the parcelable object back into our Category object
     */
    public static final Parcelable.Creator<ExpenseObject> CREATOR = new Parcelable.Creator<ExpenseObject>() {

        @Override
        public ExpenseObject createFromParcel(Parcel in) {

            return new ExpenseObject(in);
        }

        @Override
        public ExpenseObject[] newArray(int size) {

            return new ExpenseObject[size];
        }
    };
    private static final String TAG = ExpenseObject.class.getSimpleName();
    private EXPENSE_TYPES expenseType;

    /**
     * Database index of expense
     */
    private long index;

    /**
     * Title of expense
     */
    private String title;

    // TODO: Price und Expenditure in eine ValueObject "Price" extrahieren, welches dann von den PriceUtils.formatHumanReadable() lesbar gemacht wird
    /**
     * Price by user
     */
    private double price;

    /**
     * Type of expense
     * (true = expense, false = income)
     */
    private boolean expenditure;

    /**
     * Date of expense
     */
    private Calendar date;

    /**
     * Category of expense
     */
    private Category category;

    /**
     * Tags assigned to expense
     */
    private List<Tag> tags = new ArrayList<>();

    /**
     * Notice from user
     */
    private String notice;

    /**
     * Id des Zugehörigen Accounts
     */
    private long mAccountId;

    /**
     * Children of expense
     */
    private List<ExpenseObject> mChildren = new ArrayList<>();

    /**
     * Die Währung der Buchung
     */
    private Currency mCurrency;

    public ExpenseObject(long index, @NonNull String expenseName, double price, Calendar date, boolean expenditure, @NonNull Category category, String notice, long accountId, @NonNull EXPENSE_TYPES expenseType, @NonNull List<Tag> tags, @NonNull List<ExpenseObject> children, @NonNull Currency currency) {

        setIndex(index);
        setTitle(expenseName);
        setPrice(price);
        setDateTime(date != null ? date : Calendar.getInstance());
        setExpenditure(expenditure);
        setCategory(category);
        setNotice(notice != null ? notice : "");
        setAccountId(accountId);
        setExpenseType(expenseType);
        setTags(tags);
        addChildren(children);
        setCurrency(currency);
    }

    public ExpenseObject(@NonNull String title, double price, boolean expenditure, @NonNull Category category, long accountId, Currency currency) {

        this(
                -1,
                title,
                price,
                Calendar.getInstance(),
                expenditure,
                category,
                null,
                accountId,
                EXPENSE_TYPES.NORMAL_EXPENSE,
                new ArrayList<Tag>(),
                new ArrayList<ExpenseObject>(),
                currency
        );
    }

    /**
     * This will be only used by ParcelableCategories
     * see: http://prasanta-paul.blogspot.de/2010/06/android-parcelable-example.html (Parcelable ArrayList)
     * and: https://stackoverflow.com/questions/2139134/how-to-send-an-object-from-one-android-activity-to-another-using-intents for further explanations (Parcelable Object)
     * <p>
     * this constructor converts our parcelable object back into an Category object
     *
     * @param source .
     */
    public ExpenseObject(Parcel source) {

        Calendar cal = Calendar.getInstance();

        Log.v(TAG, "Recreating ExpenseObject from parcel data");
        cal.setTimeInMillis(source.readLong());
        setDateTime(cal);
        setTitle(source.readString());
        setPrice(source.readDouble());
        setIndex(source.readLong());
        setExpenditure(source.readByte() != 0);
        setCategory((Category) source.readParcelable(Category.class.getClassLoader()));
        source.readList(this.tags, Tag.class.getClassLoader());
        setNotice(source.readString());
        setAccountId(source.readLong());
        source.readList(this.mChildren, ExpenseObject.class.getClassLoader());
        setExpenseType(EXPENSE_TYPES.valueOf(source.readString()));
        setCurrency((Currency) source.readParcelable(Currency.class.getClassLoader()));
    }

    /**
     * Methode um eine dummy Booking zu erstellen
     *
     * @return dummy Booking
     */
    public static ExpenseObject createDummyExpense() {
        return new ExpenseObject(
                -1,
                app.getContext().getString(R.string.no_name),
                0,
                Calendar.getInstance(),
                false,
                Category.createDummyCategory(),
                null,
                -1,
                EXPENSE_TYPES.DUMMY_EXPENSE,
                new ArrayList<Tag>(),
                new ArrayList<ExpenseObject>(),
                Currency.createDummyCurrency()
        );
    }

    public static ExpenseObject copy(ExpenseObject other) {
        return new ExpenseObject(
                other.getIndex(),
                other.getTitle(),
                other.getUnsignedPrice(),
                other.getDateTime(),
                other.isExpenditure(),
                other.getCategory(),
                other.getNotice(),
                other.getAccountId(),
                other.getExpenseType(),
                other.getTags(),
                other.getChildren(),
                other.getCurrency()
        );
    }

    @NonNull
    public Calendar getDateTime() {

        return this.date;
    }

    public void setDateTime(@NonNull Calendar date) {

        this.date = date;
    }

    @NonNull
    public String getDate() {

        return new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(getDateTime().getTime());
    }

    @NonNull
    public String getDisplayableDateTime() {

        return DateFormat.getDateInstance(DateFormat.SHORT).format(new Date(getDateTime().getTimeInMillis()));
    }

    @NonNull
    public String getTitle() {

        return title;
    }

    public void setTitle(@NonNull String title) {

        this.title = title;
    }

    public double getUnsignedPrice() {

        return price;
    }

    /**
     * Method um den tatsächlichen wert einer Buchung zu bekommen.
     * Falls die Buchung ein Parent ist wird die Summer der Kinder zurückgegeben
     *
     * @return tatsächlicher Wert der Buchung
     */
    public double getSignedPrice() {

        if (isParent()) {

            double calcPrice = 0;

            for (ExpenseObject child : this.mChildren) {

                calcPrice += child.getSignedPrice();
            }

            return calcPrice;
        } else {

            return this.expenditure ? 0 - this.price : price;
        }
    }

    public void setPrice(double price) {

        if (price < 0) {

            this.price = Math.abs(price);
            this.expenditure = true;
        } else {

            this.price = price;
            this.expenditure = false;
        }
    }

    public long getIndex() {

        return index;
    }

    private void setIndex(long index) {

        this.index = index;
    }

    public boolean isExpenditure() {

        return expenditure;
    }

    /**
     * @param expenditure true (for outgoing money) or false (for incoming money)
     */
    public void setExpenditure(boolean expenditure) {

        this.expenditure = expenditure;
    }

    @NonNull
    public Category getCategory() {

        return category;
    }

    public void setCategory(@NonNull Category category) {

        this.category = category;
    }

    public void removeTags() {

        tags.clear();
    }

    @NonNull
    public List<Tag> getTags() {

        return tags;
    }

    /**
     * Methode um ein Liste von Tags zu einer Buchung hinzuzufügen
     *
     * @param tags Tags die der Buchung hinzugefügt werden sollen
     */
    public void setTags(@NonNull List<Tag> tags) {
        for (Tag tag : tags)
            addTag(tag);
    }

    public void addTag(@NonNull Tag tag) {

        tags.add(tag);
    }

    @NonNull
    public String getNotice() {

        return notice;
    }

    public void setNotice(String notice) {

        this.notice = notice;
    }

    public long getAccountId() {
        return mAccountId;
    }

    public void setAccountId(long accountId) {
        mAccountId = accountId;
    }

    public void setAccount(Account account) {
        mAccountId = account.getIndex();
    }

    public Currency getCurrency() {

        return mCurrency;
    }

    public void setCurrency(Currency currency) {
        mCurrency = currency;
    }

    public void removeChild(ExpenseObject child) {

        this.mChildren.remove(child);
        if (mChildren.size() == 0)
            setExpenseType(EXPENSE_TYPES.NORMAL_EXPENSE);
    }

    public void removeChildren() {
        mChildren.clear();
    }

    public ExpenseObject addChild(@NonNull ExpenseObject child) {

        child.setExpenseType(EXPENSE_TYPES.CHILD_EXPENSE);
        mChildren.add(child);
        setExpenseType(EXPENSE_TYPES.PARENT_EXPENSE);

        return this;
    }

    /**
     * Methode um einer Buchung mehrere Buchungen hinzuzufügen
     *
     * @param children Buchungen die als Kinder hinzugefügt werden sollen
     */
    public void addChildren(@NonNull List<ExpenseObject> children) {
        for (ExpenseObject childExpense : children)
            addChild(childExpense);
    }

    @NonNull
    public List<ExpenseObject> getChildren() {

        return this.mChildren;
    }

    public boolean isParent() {

        return expenseType == EXPENSE_TYPES.PARENT_EXPENSE;
    }

    /**
     * Methode die die Felder der Buchung checkt ob diese gesetzt sind oder nicht.
     * Sind alle Felder gesetzt, dann kann die Buchung ohne Probleme in die Datenbank geschrieben werden.
     *
     * @return Ob die Buchung in die Datenbank geschrieben werden kann
     */
    public boolean isSet() {

        return !this.title.equals(app.getContext().getString(R.string.no_name))
                && this.price != 0
                && this.category.isSet()
                && this.mAccountId != -1;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ExpenseObject))
            return false;

        ExpenseObject otherExpense = (ExpenseObject) obj;

        boolean result = getTitle().equals(otherExpense.getTitle());
        result = result && (getUnsignedPrice() == otherExpense.getUnsignedPrice());
        result = result && getAccountId() == otherExpense.getAccountId();
        result = result && getExpenseType().equals(otherExpense.getExpenseType());
        result = result && getDateTime().getTimeInMillis() == otherExpense.getDateTime().getTimeInMillis();
        result = result && getNotice().equals(otherExpense.getNotice());
        result = result && getCategory().getIndex() == otherExpense.getCategory().getIndex();//ich kann die objekte nicht vergleichen da parent buchungen nur dummies bekommen
        result = result && getCurrency().getIndex() == otherExpense.getCurrency().getIndex();//ich kann die objekte nicht vergleichen da parent buchungen nur dummies bekommen

        for (Tag tag : getTags()) {
            for (Tag otherTag : otherExpense.getTags()) {
                result = result && tag.equals(otherTag);
            }
        }

        for (ExpenseObject child : getChildren()) {
            result = result && otherExpense.getChildren().contains(child);
        }

        return result;
    }

    @Override
    public String toString() {

        return getIndex() + " " + getTitle() + " " + getUnsignedPrice();
    }

    /**
     * Wenn die Buchung den Buchungstyp Normal oder Parent hat, dann ist die Buchung Valid.
     *
     * @return Ist die Buchung vom typ Normal oder Parent
     */
    public boolean isValidExpense() {

        return this.expenseType == EXPENSE_TYPES.NORMAL_EXPENSE || this.expenseType == EXPENSE_TYPES.PARENT_EXPENSE || expenseType == EXPENSE_TYPES.CHILD_EXPENSE;
    }

    @NonNull
    public EXPENSE_TYPES getExpenseType() {

        return this.expenseType;
    }

    public void setExpenseType(@NonNull EXPENSE_TYPES expenseType) {

        this.expenseType = expenseType;
    }

    /**
     * can be ignored mostly
     *
     * @return int
     */
    @Override
    public int describeContents() {

        return 0;
    }

    /**
     * converting the custom object into an parcelable object
     *
     * @param dest  destination Parcel
     * @param flags flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {

        Log.v(TAG, "Writing ExpenseObject to parcel" + flags);
        dest.writeLong(date.getTimeInMillis());
        dest.writeString(title);
        dest.writeDouble(price);
        dest.writeLong(index);
        dest.writeByte((byte) (expenditure ? 1 : 0));
        dest.writeParcelable(category, flags);
        dest.writeList(tags);
        dest.writeString(notice);
        dest.writeLong(mAccountId);
        dest.writeList(mChildren);
        dest.writeString(expenseType.name());
        dest.writeParcelable(mCurrency, flags);
    }

    public enum EXPENSE_TYPES {
        DUMMY_EXPENSE,
        DATE_PLACEHOLDER,
        PARENT_EXPENSE,
        NORMAL_EXPENSE,
        TRANSFER_EXPENSE,
        CHILD_EXPENSE
    }
}
