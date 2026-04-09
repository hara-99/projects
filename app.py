

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

