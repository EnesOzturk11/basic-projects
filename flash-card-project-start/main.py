import tkinter as tk
import random
import pandas

BACKGROUND_COLOR = "#B1DDC6"
OK="➡"
side=1
last_word_eng=[""]
last_word_fre=[""]
unknown_fre=dict()
unknown_eng=dict()

window = tk.Tk()
window.title("Flash Card")
window.config(padx=50, pady=50, background=BACKGROUND_COLOR)


def generator():   #select a random word from frenc_words.csv
    data=pandas.read_csv("python/day31/flash-card-project-start/data/french_words.csv")
    dictionary={row.French:row.English for (index,row) in data.iterrows()}
    keys=dictionary.keys()
    french_word=random.choice(list(keys))
    english_word=dictionary[french_word]
    last_word_eng[0]=english_word
    last_word_fre[0]=french_word
    return (french_word,english_word)


def wrong(): 
    french,eng=generator()
    if side==1:
        canvas.itemconfig(title_text,text="French")
        canvas.itemconfig(word_text,text=french)
        last_word_fre[0]=french
        unknown_fre[french]=eng

    else:
        canvas.itemconfig(title_text,text="English")
        canvas.itemconfig(word_text,text=eng)
        last_word_eng[0]=eng
        unknown_eng[eng]=french


def right():
    french,eng=generator()
    if side==1:
        canvas.itemconfig(title_text,text="French")
        canvas.itemconfig(word_text,text=french)
        last_word_fre[0]=french


    else:
        canvas.itemconfig(title_text,text="English")
        canvas.itemconfig(word_text,text=eng)
        last_word_eng[0]=eng
    

def flip():
    global side
    if side==1:
        side=0
        canvas.itemconfig(card_background_img,image=card_back_img)
        canvas.itemconfig(title_text,text="English")
        canvas.itemconfig(word_text,text=last_word_eng[0])
 
    else:
        side=1
        canvas.itemconfig(card_background_img,image=card_front_img)
        canvas.itemconfig(title_text,text="French")
        canvas.itemconfig(word_text,text=last_word_fre[0])


def close():
    info_page=tk.Tk()
    info_page.title("Unknown Words")


    eng_part=""
    for key,value in unknown_eng.items():
        eng_part+=f"{key}{OK}{value}\n"
    fre_part=""
    for key,value in unknown_fre.items():
        fre_part+=f"{key}{OK}{value}\n"
    text=f"English Part\t\tFrench Part\n{eng_part}\t\t{fre_part}"
 


    eng_tittle=tk.Label(master=info_page,text="English",width=30)
    eng_label=tk.Label(master=info_page,text=eng_part,width=30)
    eng_label.grid(row=1,column=0)
    eng_tittle.grid(row=0,column=0)

    fre_tittle=tk.Label(master=info_page,text="French",width=30)
    fre_label=tk.Label(master=info_page,text=fre_part)
    fre_label.grid(row=1,column=1)
    fre_tittle.grid(row=0,column=1)
    window.destroy()

  



canvas = tk.Canvas(width=800, height=526,highlightthickness=0,highlightbackground=BACKGROUND_COLOR)
card_front_img = tk.PhotoImage(file="python/day31/flash-card-project-start/images/card_front.png")
card_back_img=tk.PhotoImage(file="python/day31/flash-card-project-start/images/card_back.png")
card_background_img=canvas.create_image(400, 263, image=card_front_img)
title_text=canvas.create_text(400,150,text="",font=("Ariel",40,"italic"),fill="black")
word_text=canvas.create_text(400,263,text="",font=("Ariel",60,"bold"),fill="black")

canvas.grid(row=1,column=0,columnspan=2)  
canvas.config(background=BACKGROUND_COLOR)

flip_button_img=tk.PhotoImage(file="python/day31/flash-card-project-start/images/change_button.png")
flip_button=tk.Button(master=window,image=flip_button_img,highlightthickness=0,command=flip,highlightbackground=BACKGROUND_COLOR)
flip_button.grid(row=3,column=0,columnspan=2)

cross_img=tk.PhotoImage(file="python/day31/flash-card-project-start/images/wrong.png")
wrong_button=tk.Button(master=window,image=cross_img,highlightbackground=BACKGROUND_COLOR,highlightthickness=0,highlightcolor=BACKGROUND_COLOR,command=wrong)
wrong_button.grid(row=2,column=0)

tik_img=tk.PhotoImage(file="python/day31/flash-card-project-start/images/right.png")
right_button=tk.Button(master=window,image=tik_img,highlightbackground=BACKGROUND_COLOR,highlightthickness=0,highlightcolor=BACKGROUND_COLOR,command=right)
right_button.grid(row=2,column=1)

close_img=tk.PhotoImage(file="python/day31/flash-card-project-start/images/pasted-movie.png")
close_button=tk.Button(master=window,highlightthickness=0,highlightbackground=BACKGROUND_COLOR,command=close,image=close_img,highlightcolor=BACKGROUND_COLOR)
close_button.grid(row=0,column=3)


right()
window.mainloop()