"""Insert ~100 dummy spend entries over the last two months for user 1."""

from __future__ import annotations

import random
from datetime import date, timedelta
from pathlib import Path

USER_ID = 1
END = date(2026, 9, 4)
START = date(2026, 7, 4)
ENTRY_COUNT = 100

TEMPLATES = [
    ("Food", "Lunch at cafe", 120, 480, "Weekday lunch"),
    ("Food", "Swiggy dinner", 220, 650, "Delivered at home"),
    ("Food", "Grocery run", 450, 2200, "Weekly groceries"),
    ("Food", "Coffee and snack", 80, 250, None),
    ("Food", "Dinner with friends", 700, 2400, "Split later"),
    ("Food", "Breakfast stall", 60, 180, None),
    ("Transport", "Uber to office", 90, 320, None),
    ("Transport", "Metro card recharge", 200, 600, None),
    ("Transport", "Petrol fill-up", 800, 2500, None),
    ("Transport", "Auto rickshaw", 40, 160, None),
    ("Transport", "Parking fee", 30, 120, None),
    ("Utilities", "Electricity bill", 900, 2800, "Monthly bill"),
    ("Utilities", "JioFiber internet", 699, 1299, "Broadband"),
    ("Utilities", "Mobile recharge", 199, 599, None),
    ("Utilities", "Water bill", 250, 700, None),
    ("Shopping", "Amazon order", 299, 2499, None),
    ("Shopping", "Clothes shopping", 800, 3500, None),
    ("Shopping", "Household supplies", 180, 900, None),
    ("Shopping", "Electronics accessory", 350, 1800, None),
    ("Health", "Pharmacy medicines", 120, 850, None),
    ("Health", "Clinic consultation", 400, 1500, "Walk-in visit"),
    ("Health", "Gym day pass", 200, 500, None),
    ("Other", "Movie tickets", 250, 900, None),
    ("Other", "Gift", 400, 2000, None),
    ("Other", "Streaming subscription", 149, 649, "Monthly plan"),
]

BUDGETS = [
    ("Food", 18000),
    ("Transport", 6000),
    ("Utilities", 4500),
    ("Shopping", 9000),
    ("Health", 3500),
    ("Other", 4000),
]

RECURRING = [
    ("JioFiber internet", "Utilities", 999, "MONTHLY", "Home broadband"),
    ("Netflix", "Other", 649, "MONTHLY", "Family plan"),
    ("Gym membership", "Health", 1500, "MONTHLY", "Local gym"),
    ("Metro commute pass", "Transport", 800, "MONTHLY", None),
]


def sql_str(value: str | None) -> str:
    if value is None:
        return "NULL"
    return "'" + value.replace("'", "''") + "'"


def main() -> None:
    rng = random.Random(42)
    span = (END - START).days
    rows: list[str] = []

    for i in range(ENTRY_COUNT):
        category, description, low, high, note = rng.choice(TEMPLATES)
        # Spread dates across the window, with a little jitter so it is not uniform.
        day_offset = int(i * span / (ENTRY_COUNT - 1))
        day_offset = min(span, max(0, day_offset + rng.randint(-2, 2)))
        expense_date = START + timedelta(days=day_offset)
        amount = round(rng.uniform(low, high), 2)
        if rng.random() < 0.55:
            note = None
        rows.append(
            f"  ({USER_ID}, {sql_str(description)}, {sql_str(category)}, {amount:.2f}, '{expense_date.isoformat()}', {sql_str(note)})"
        )

    budget_rows = [
        f"  ({USER_ID}, {sql_str(category)}, {amount:.2f}, 'MONTHLY')"
        for category, amount in BUDGETS
    ]
    recurring_rows = [
        f"  ({USER_ID}, {sql_str(description)}, {sql_str(category)}, {amount:.2f}, '{period}', {sql_str(note)})"
        for description, category, amount, period, note in RECURRING
    ]

    spend_values = ",\n".join(rows)
    budget_values = ",\n".join(budget_rows)
    recurring_values = ",\n".join(recurring_rows)
    sql = f"""BEGIN;

DELETE FROM spend_entries WHERE user_id = {USER_ID};
DELETE FROM budgets WHERE user_id = {USER_ID};
DELETE FROM recurring_expenses WHERE user_id = {USER_ID};

INSERT INTO spend_entries (user_id, description, category, amount, expense_date, note)
VALUES
{spend_values};

INSERT INTO budgets (user_id, category, amount, period)
VALUES
{budget_values};

INSERT INTO recurring_expenses (user_id, description, category, amount, period, note)
VALUES
{recurring_values};

COMMIT;
"""
    out = Path(__file__).with_name("seed_dummy_data.sql")
    out.write_text(sql, encoding="utf-8")
    print(f"Wrote {out} with {ENTRY_COUNT} spend entries")


if __name__ == "__main__":
    main()
