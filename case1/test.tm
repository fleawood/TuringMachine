; we can achieve the goal by 3 steps
; first, check whether the input is the form 1*x1*=1*
; second, subtract the number of 1 after = to 0
; third, check whether the result is the form x1*=

#Q = {q0,q1,q2,q3,q4,q5,q6,q7,q8,q9,q10,q11,q12,q13,acc,acc2,acc3,acc4,rej,rej2,rej3,rej4,rej5,reg6,halt_acc,halt_rej}
#S = {1,=,x}
#T = {1,=,x,_,0,T,r,u,e,F,a,l,s}
#q0 = q0
#B = _
#F = {halt_acc}

; State q0: check 1 before x
q0 1 1 r q0
q0 x x r q1
q0 * * l rej

; State q1: check 1 between x and =
q1 1 1 r q1
q1 = = r q2
q1 * * l rej

; State q2: check 1 after =
q2 1 1 r q2
q2 _ _ l q3
q2 * * l rej

; State q3: return to left end
q3 * * l q3
q3 _ _ r q4

; State q4: erase the leftmost 1
q4 1 _ r q5
q4 x _ r q12

; State q5: go through 1 before x
q5 1 1 r q5
q5 x x r q6

; State q6: mark the first 1 between x and =
q6 1 0 r q7 ; mark
q6 0 0 r q6
q6 = = l q10 ; all 1 are marked, now clear

; State q7: go to right end
q7 * * r q7
q7 _ _ l q8

; State q8: erase the rightmost 1
q8 1 _ l q9
q8 = = l rej

; State q9: go left until meet x
q9 * * l q9
q9 x x r q6 ; go to mark the next one

; State q10: erase all marks
q10 0 1 l q10
q10 x x l q11

; State q11: go to left end
q11 1 1 l q11
q11 _ _ r q4

; State q12: go through 1 between x and =
q12 1 _ r q12
q12 = _ r q13

; State q13: check if any 1 after =
q13 1 1 l rej
q13 _ _ r acc

; State rej*: clear the tape, write 'False'
rej * * l rej
rej _ _ r rej2
rej2 * _ r rej2
rej2 _ F r rej3
rej3 _ a r rej4
rej4 _ l r rej5
rej5 _ s r rej6
rej6 _ e * halt_rej

; State acc*: clear the tape, write 'True'
acc * _ r acc
acc _ T r acc2
acc2 _ r r acc3
acc3 _ u r acc4
acc4 _ e * halt_acc