PostgreSQL
----------

```
Users Table (
    username primary varchar(100)
    password varchar(1000)  -- salted and hashed
);

Portfolio Table (
    portfolio_id primary serial
    portfolio_name varchar(100)
    username varchar(100)
);

Portfolio Holdings Table (
    portfolio_id int
    ticker varchar(10)
    quantity int
    purchase_time datetime
    purchase_price decimal
);
```
