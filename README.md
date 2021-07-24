<h1 align="center">Email Classifier</h1>
<h2 align="center">A simple classifier bases on Naive Bayes algorithm</h2>

# [**Table Of Content**](#table-of-content)
- [**Table Of Content**](#table-of-content)
- [**Attention**](#attention)
- [**How to run**](#how-to-run)
- [**How it works**](#how-it-works)

# [**Attention**](#attention)
It just only classifier for `txt` file.
The training database is so tiny if you want to increase its accurary.You have to prepare more `txt` files to train

# [**How to run**](#how-to-run)
1. Just download it and open Elipse.
   
2. Click File > Import > General.
   
3. Click Existing Projects into Workspace. You can edit the project directly in its original location or choose to create a copy of the project in the workspace

# [**How it works**](#how-it-works)
1. I write a training file.In this file, I prepare 2 array list : Spam-Keyword-Collection & Nonspam-Keyword-Collection.They will add collections into them.
   
2. Each collection will be like this: [ i , am , phong , kaster ].
   
3. Both Spam-Keyword-Collection & Nonspam-Keyword-Collection will be like this: [ [i, am, phong, kaster],[today, is, nice],[........],[........] ].
   
4. After reading all files in spam folder & non-spam folder.The outcome is stored in `outcome.dat` file.
   
5. When execution.java is run, it calls `outcome.dat` file and then calculate Spam rate & Nonspam rate following Naive Bayes algorithm.
   
6. Finally, Spam rate is taken compare with Non-spam Rate & conclusion.
