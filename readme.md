**Project architecture**

Implementation of a banking application containing: user registration and authentication, each user having one or multiple accounts - current, deposits, loans + operations (create, delete, withdraw, deposit, transfer, recurring interests etc.) on them, and the separate option of making card payments.

**Project architecture**
- Client
- Server
    - Model
    - View
        - Menu
          - Main Menu: login, register, pay with card
          - Payment menu: pay amount with a physical card (pin requested) or wireless (pin requested only after a certain amount), return to main
          - Authenticated menu: create and view current accounts, loans and deposits, check menu for a specific current account, view transactions for loans and deposits, add days to check scheduled payments and delete user, logout
          - Account menu: check current balance, transactions, deposit or withdraw cash, transfer money to another account, create a card for the account, view cards, freeze, unfreeze or remove any of them and delete account, back to authenticated
        - Input
          - Input validator: checkers for user input: amount, username, password, email, pin, id etc.
          - Input hashing: sha 256 for password security
        - Messages: success messages, error messages, input fields, commands for the menus etc.
    - Service
        - User service
        - Account service
        - Transaction service
        - Card service
    - Repository
        - User repository
        - Account repository
        - Account type repository
        - Transaction repository
        - Card repository
        - Database connection
    - Scheduler: perform updates for monthly due and expired deposits and loans


- Unit testing:
  - Services
  - Scheduler
  - Direct input checkers and hashing
