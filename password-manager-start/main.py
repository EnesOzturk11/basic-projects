from tkinter import *
from tkinter import messagebox   #call messagebox
import random
import pyperclip  #call this module whose purpose is to copy or paste something
import json
# ---------------------------- PASSWORD GENERATOR ------------------------------- #
def generator():
    letters = ['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z']
    numbers = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9']
    symbols = ['!', '#', '$', '%', '&', '(', ')', '*', '+']

    nr_letters = random.randint(8, 10)
    nr_symbols = random.randint(2, 4)
    nr_numbers = random.randint(2, 4)

    password_list = []

    password_list+=[random.choice(letters) for i in range(nr_letters)]
    password_list+=[random.choice(numbers) for i in range(nr_symbols)]
    password_list+=[random.choice(symbols) for i in range(nr_numbers)]

    random.shuffle(password_list)

    password="".join(password_list)
    pass_input.delete(0,END)
    pass_input.insert(0,password)
    pyperclip.copy(password)

# ---------------------------- SAVE PASSWORD ------------------------------- #
def save():
    web=web_name.get()
    usr=user_name.get()
    pwd=pass_input.get()

    new_data={
        web:{
            "email":usr,
            "password":pwd
        }
    }

    if len(web)==0 or len(usr)==0 or len(pwd)==0:
        messagebox.showinfo(title="Oops",message="Please don't leave any fields empty")
    else:
        try:
            with open("./day29/password-manager-start/save.json","r") as file:
                data=json.load(file)
        except FileNotFoundError:
            with open("./day29/password-manager-start/save.json","w") as file:
                json.dump(new_data,file,indent=4)
        else:        
            data.update(new_data)

            with open("./day29/password-manager-start/save.json","w") as file:
                json.dump(data,file,indent=4)
        finally:
            web_name.delete(0,END)    #delete words in range which you can edit in  entry block, arg:first=start,last=stop
            pass_input.delete(0,END)

#----------------------------- SEARCH ----------------------------------#  
def search():
    input_web=web_name.get()
    try:
        with open("./day29/password-manager-start/save.json","r") as file:
            data=json.load(file)
    
    except FileNotFoundError:
        messagebox.showerror(title="ERROR",message="No Data File Found")

    else:
        try:
            pwd=data[input_web]["password"]
            messagebox.showinfo(title=input_web,message=f"Email:{user_name.get()}\nPassword:{pwd}")
        except:
            messagebox.showerror(title="No",message="No Detail")
# ---------------------------- UI SETUP ------------------------------- #

window=Tk()
window.title("Password Manager")
window.minsize(width=240,height=240)
window.config(padx=20,pady=20)

canvas=Canvas(master=window,width=200,height=200,highlightthickness=0)
lock_image=PhotoImage(file="./day29/password-manager-start/logo.png")
canvas.create_image(100,100,image=lock_image)
canvas.grid(row=0,column=1)

web_label=Label(master=window,text="Website:",width=15)
web_label.grid(row=1,column=0)

user_label=Label(master=window,text="Email/Username:",width=15)
user_label.grid(row=2,column=0)

password_label=Label(master=window,text="Password",width=15)
password_label.grid(row=3,column=0)

web_name=Entry(master=window,width=25)
web_name.focus()
web_name.grid(row=1,column=1)

user_name=Entry(master=window,width=35)
user_name.grid(row=2,column=1,columnspan=2)
user_name.insert(END,"enes.ozturk")

pass_input=Entry(master=window,width=21)
pass_input.grid(row=3,column=1)

search_button=Button(master=window, text="Search",width=10,command=search)
search_button.grid(row=1,column=2)

generate_button=Button(master=window,text="Generate Password",command=generator)
generate_button.grid(row=3,column=2)

add_button=Button(master=window,text="Add",width=36,command=save)
add_button.grid(row=4,column=1,columnspan=2)



window.mainloop()