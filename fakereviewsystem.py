# ===============================
# INSTALL LIBRARIES
# ===============================
!pip install streamlit pyngrok vaderSentiment -q

# ===============================
# CREATE STREAMLIT APP
# ===============================
app_code = """

import streamlit as st
from vaderSentiment.vaderSentiment import SentimentIntensityAnalyzer
analyzer = SentimentIntensityAnalyzer()

st.title("Fake Review Detection System")

st.write("Enter the product rating and review")

review = st.text_area("Enter Review")

rating = st.slider("Product Rating",1,5)
if st.button("Predict"):

    sentiment = analyzer.polarity_scores(review)["compound"]

    if rating >=4 and sentiment < 0:
        st.error("Fake Review")

    elif rating <=2 and sentiment > 0:
        st.error("Fake Review")

    else:
        st.success("Genuine Review")

"""

with open("app.py","w") as f:
    f.write(app_code)
# ===============================
# START STREAMLIT
# ===============================
from pyngrok import ngrok

ngrok.set_auth_token("3AaS1d0ub1bp3HETaZL9zwKgH0z_6m4VFX4XdFMvHzvGexDrj")

url = ngrok.connect(8501)

print("Open this link:",url)

!streamlit run app.py &f
