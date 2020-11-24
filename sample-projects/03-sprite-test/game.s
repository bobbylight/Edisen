; NES 2.0 header
.segment "HEADER"
    .byte "NES"
    .byte $1a
    .byte $02
    .byte $01
    .byte %00000000
    .byte $00
    .byte $00
    .byte $00
    .byte $00
    .byte $00,$00,$00,$00,$00

; ???
.segment "STARTUP"

; Configure variables in zero page memory
.segment "ZEROPAGE"
    flag: .res 1
    counter: .res 1

; The actual game code
.segment "CODE"

; Subroutines are denoted by labels.
; This waits for a vblank
WAITVBLANK:
:
    BIT $2002
    BPL :-
    RTS

; Runs when game starts up (and reset is pressed?).
; This is mostly boilerplate found in most games.
RESET:
  SEI          ; disable IRQs
  CLD          ; disable decimal mode
  LDX #$40
  STX $4017    ; disable APU frame IRQ
  LDX #$FF
  TXS          ; Set up stack
  INX          ; now X = 0 ( == 0xff + 1)
  STX $2000    ; disable NMI
  STX $2001    ; disable rendering
  STX $4010    ; disable DMC IRQs

  JSR WAITVBLANK

; Clear all memory before the game starts.
; This is mostly boilerplate found in most games.
clrmem:
  LDA #$00
  STA $0000, x ; Set 0x0000 - 0x00ff to 0 via loop
  STA $0100, x ; Set 0x0100 - 0x01ff to 0 via loop
  STA $0200, x ; etc.
  STA $0400, x
  STA $0500, x
  STA $0600, x
  STA $0700, x
  LDA #$FE
  STA $0300, x ; Set 0x0300 - 0x03ff to 0xfe via loop (why)
  INX
  BNE clrmem ; Loop whle x != 0

  LDA #%10001000
  STA flag
   
  JSR WAITVBLANK

  LDA #%00000000
  STA counter
  STA $2001
  LDA #%10001000
  STA $2000
  LDA #$3F
  STA $2006
  LDA #$00
  STA $2006
  STA $2007
  CLI
Forever:
  JMP Forever  
  
VBLANK:
  INC counter
  LDA counter
  CMP #$3C
  BNE SkipColorChange
  LDA flag
  EOR #%10000000
  STA flag
  STA $2001
  LDA #$00
  STA counter
 SkipColorChange:
  RTI

.segment "VECTORS"
    .word VBLANK
    .word RESET
    .word 0

; There are no graphics in this game
.segment "CHARS"
