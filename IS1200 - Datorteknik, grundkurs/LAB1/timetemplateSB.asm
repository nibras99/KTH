  # timetemplate.asm
  # Written 2015 by F Lundevall
  # Copyright abandonded - this file is in the public domain.

.macro	PUSH (%reg)
	addi	$sp,$sp,-4
	sw	%reg,0($sp)
.end_macro

.macro	POP (%reg)
	lw	%reg,0($sp)
	addi	$sp,$sp,4
.end_macro

	.data
	.align 2
mytime:	.word 0x4050
timstr:	.ascii "text more text lots of text\0"
	.text
main:
	# print timstr
	la	$a0,timstr
	li	$v0,4
	syscall
	nop
	# wait a little
	li	$a0,2
	jal	delay
	nop
	# call tick
	la	$a0,mytime
	jal	tick
	nop
	# call your function time2string
	la	$a0,timstr
	la	$t0,mytime
	lw	$a1,0($t0)
	jal	time2string
	nop
	# print a newline
	li	$a0,10
	li	$v0,11
	syscall
	nop
	# go back and do it all again
	j	main
	nop
# tick: update time pointed to by $a0
tick:	lw	$t0,0($a0)	# get time
	addiu	$t0,$t0,1	# increase
	andi	$t1,$t0,0xf	# check lowest digit
	sltiu	$t2,$t1,0xa	# if digit < a, okay
	bnez	$t2,tiend
	nop
	addiu	$t0,$t0,0x6	# adjust lowest digit
	andi	$t1,$t0,0xf0	# check next digit
	sltiu	$t2,$t1,0x60	# if digit < 6, okay
	bnez	$t2,tiend
	nop
	addiu	$t0,$t0,0xa0	# adjust digit
	andi	$t1,$t0,0xf00	# check minute digit
	sltiu	$t2,$t1,0xa00	# if digit < a, okay
	bnez	$t2,tiend
	nop
	addiu	$t0,$t0,0x600	# adjust digit
	andi	$t1,$t0,0xf000	# check last digit
	sltiu	$t2,$t1,0x6000	# if digit < 6, okay
	bnez	$t2,tiend
	nop
	addiu	$t0,$t0,0xa000	# adjust last digit
tiend:	sw	$t0,0($a0)	# save updated result
	jr	$ra		# return
	nop

  # Kod nedan skriven av ME
  #
hexasc:
	andi	$a0, $a0, 0x0f	# Kopierar dem 4 minsta bitarna, alla andra ignoreras
	move	$t0, $a0	# Sparar den i t0
	
	li	$t3, 0x09	# t3 s�tts som 9, f�r att kunna kolla om det �r en siffra eller bokstav senare
	slt	$t1, $t3, $a0	# Kontrollerar om talet �r st�rre �n 9, st�mmer det, t1 == 1
	mul	$t2, $t1, 0x07	# t2 blir t1 g�nger 7, allts� blir det 0 eller 7
	add	$t0, $t0, $t2	# Om talet �r en bokstav s� kommer 7 adderas f�r att allt ska bli r�tt i tabellen 
	
	addi	$t0, $t0, 0x30	# F�rflyttar hela 30 steg
	move	$v0, $t0	# F�rbereder return genom att spara i v0
	
	jr	$ra		# Returnerar
	nop			# delay slot filler (just in case)

delay:
	jr $ra
	nop
	
time2string:
	move	$t9, $a0		# "kopierar" �ver adressen till mytime
	PUSH	($ra) 			# Sparar s� att vi inte skriver �ver
	PUSH	($s1) 			# ^^^
	PUSH	($s2) 			# ^^
	move	$s1, $a1		# Kopierar �ver v�rdet i adressen till s1

	# Minutplatsen l�nst till v�nster
	andi	$a0, $s1, 0xf000	# Genom andi s� g�r vi allt utom de fyra bitarna vi vill beh�lla till nollor.
	srl	$a0, $a0, 12		# F�rflyttar dem fyra bitarna l�ngst till h�ger, allts� mot LSB
	jal	hexasc			# Konverterar till ASCII
	nop
	move	$s2, $v0		# Sparar s2 som det returnerade ASCII v�rdet
	# F�r talet 57:58 ser det nu ut som
	# 0x00003537
	
	# Minutplatsen till v�nster
	andi	$a0, $s1, 0x0f00	# Genom andi s� g�r vi allt utom de fyra bitarna vi vill beh�lla till nollor.
	srl	$a0, $a0, 8		# F�rflyttar dem fyra bitarna l�ngst till h�ger, allts� mot LSB
	jal	hexasc			# Konverterar till ASCII
	nop
	sll	$v0, $v0, 8		# F�rflyttar 0x00000035 till 0x00003500
	add	$s2, $s2, $v0		# Adderar s� att talet blir 0x00003537
	# F�r talet 57:58 ser det nu ut som
	# 0x00003537
	
	# Kolon
	li	$t2, 0x3A		# ASCII koden f�r kolon
	sll	$t2, $t2, 16		# F�rflyttar 0x00003537 till 0x00353700
	add	$s2, $s2, $t2		# Adderar s� att talet nu �r 0x0035373A
	
	# Minutplatsen till h�ger
	andi	$a0, $s1, 0x00f0	# samesame
	srl	$a0, $a0, 4		# samesame
	jal	hexasc			# samesame
	nop
	sll	$v0, $v0, 24		# F�rflyttar 0x0035373A till 0x35373A00
	add	$s2, $s2, $v0		# Adderar s� att talet nu �r 0x35373A35
	
	# Sekundplatsen till v�nster
	sw	$s2, 0($t9)		# s2 �r nu full, s� ist�llet f�r att anv�nda en till s0 s� sparar vi den till minnet
	
	# Sekundplatsen till h�ger
	andi	$a0, $s1, 0x000f	# H�mtar sista biten
	jal	hexasc			# Beh�ver inte f�rflytta, s� konvertera direkt.
	nop
	sw	$v0, 4($t9)		# Sparar den sista biten direkt till minnet

	POP	($s2)			# Laddar tillbaka fr�n minnet
	POP	($s1)			# ^^^
	POP	($ra)			# ^^
	jr	$ra
	nop
