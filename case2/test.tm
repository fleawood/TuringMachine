#Q = {q0,q1,q2,q3,q4,q5,q6,q7,q8,acc,acc2,acc3,acc4,rej,halt_acc,halt_rej}
#S = {a,b}
#T = {a,b,_,0,1,x,T,r,u,e,F,l,s}
#q0 = q0
#B = _
#F = {halt_acc}

; State q0: mark the next a(b) to 0(1)
q0 _ _ r acc ; empty string, accept
q0 a 0 r q1
q0 b 1 r q1
q0 * * l q4 ; when see 0 or 1, it means the whole string is marked

; State q1: find the rightmost unmarked symbol
q1 a a r q1
q1 b b r q1
q1 * * l q2 ; when see 0, 1 or B, it means we reach the right end

; State q2: mark the next a(b) to 0(1)
q2 a 0 l q3
q2 b 1 l q3
q2 * * l rej ; only happened when the length is odd, reject

; State q3: find the leftmost unmarked symbol
q3 a a l q3
q3 b b l q3
q3 * * r q0 ; when see 0, 1 or B, it means we reach the left end

; State q4: clear marks in the left half
q4 0 a l q4 ; clear marks
q4 1 b l q4
q4 _ _ r q5 ; reach the start

; State q5: get next symbol
q5 a _ r q6 ; the first symbol is a
q5 b _ r q7 ; the first symbol is b
q5 x _ r acc ; when see x, it means all left half symbol are checked

; State q6: check if the first symbol in right half is 0
q6 * * r q6
q6 0 x l q8 ; matched, go back to check the next pair
q6 1 1 l rej ; not matched, reject

; State q7: check if the first symbol in right half is 1
q7 * * r q7
q7 1 x l q8 ; matched, go back to check the next pair
q7 0 0 l rej ; not matched, reject

; State q8: go back to find the next symbol
q8 * * l q8
q8 _ _ r q5 ; reach the left end

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