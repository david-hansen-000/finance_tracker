package home.david.finances;

public class SimpleBill {
    private String name;
    private String duedate;
    private String amount;

    public SimpleBill() {
        name=null;
        duedate=null;
        amount=null;
    }

    public SimpleBill(String name, String duedate, String amount) {
        this.name = name;
        this.duedate = duedate;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDuedate() {
        return duedate;
    }

    public void setDuedate(String duedate) {
        this.duedate = duedate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public TableRow getTableRow() {
        TableRow result=new TableRow();
        result.addRowItem("bill", name);
        result.addRowItem("amount", amount);
        result.addRowItem("duedate", amount);
        return result;
    }
}
