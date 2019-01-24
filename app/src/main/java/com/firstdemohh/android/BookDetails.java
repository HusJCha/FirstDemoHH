package com.firstdemohh.android;

public class BookDetails {

    public String bnm;
    public Integer bprice;
    public String bdate;


    public Integer getBprice()
    {
        return bprice;
    }

    public void setBprice(Integer bprice)
    {
        this.bprice = bprice;
    }

    public String getBdate()
    {
        return bdate;
    }

    public void setBdate(String bdate)
    {
        this.bdate = bdate;
    }

    public String getBnm() {
        return bnm;
    }

    public void setBnm(String bnm) {
        this.bnm = bnm;
    }
}
