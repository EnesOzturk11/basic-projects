##################### Extra Hard Starting Project ######################
import pandas as pd
import datetime
import random
import smtplib

user="csdeneme35@gmail.com"
password="ztlu lcng hnmc eewa"
# 1. Update the birthdays.csv
contex=pd.read_csv("day32/birthday-wisher-extrahard-start/birthdays.csv")
data={row["name"]:[row.email,row.year,row.month,row.day] for (index,row) in contex.iterrows()}
print(data)

date=datetime.datetime.now()
year=date.year
month=date.month
day=date.day

message=""
checker=False
letters=["day32/birthday-wisher-extrahard-start/letter_templates/letter_1.txt","day32/birthday-wisher-extrahard-start/letter_templates/letter_2.txt","day32/birthday-wisher-extrahard-start/letter_templates/letter_3.txt"]

person=None
# 2. Check if today matches a birthday in the birthdays.csv
for name in data:
    if (data[name][2]==month and data[name][3]==day):
        person=name
        checker=True
        break
# 3. If step 2 is true, pick a random letter from letter templates and replace the [NAME] with the person's actual name from birthdays.csv
if checker:
    with open(random.choice(letters),"r+") as file:
        message=file.read()
        message=message.replace("[NAME]",str(name))

# 4. Send the letter generated in step 3 to that person's email address.
with smtplib.SMTP("smtp.gmail.com") as connect:
    connect.starttls()
    connect.login(user=user, password=password)
    connect.sendmail(from_addr=user, to_addrs=data[name][0],msg=f"Subject:Happy Birthday\n\n{message}")




