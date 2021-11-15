import os

#  PATH TO FINITE AUTOMATA FILE
from utils import read_fa_from_file, read_fa_from_user

fa_path = os.path.join(os.getcwd(), "fa.in")

finite_automata = read_fa_from_file(fa_path)

print(finite_automata.is_seq_accepted('01'))
while True:
    print("1. Show the transitions and the states.\n")
    print("2. Show alphabet.\n")
    print("3. Show final states.\n")
    print("4. Show start state.\n")
    print("5. Show all of the above.\n")
    print("0. Exit\n")
    x = int(input("Command: "))
    if x == 1:
        print(finite_automata.get_transitions())
    if x == 2:
        print(str(finite_automata.get_alphabet()) + "\n")
    if x == 3:
        print(finite_automata.get_final_states())
    if x == 4:
        print(finite_automata.get_start_state())
    if x == 5:
        print(finite_automata)
    if x == 0:
        break

