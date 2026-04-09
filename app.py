import streamlit as st
import requests

API_KEY = "c10f18f0bc15eebaa3746866e67180b5"

st.set_page_config(page_title="Weather Prediction App", page_icon="🌦", layout="centered")

st.title("🌦 Automated Weather Prediction System")
st.markdown("---")

st.markdown("### 📍 Enter City Name to Get Weather Prediction")

# City input
city = st.text_input("City Name:", placeholder="e.g., London, New York, Tokyo")

# Get Weather button
if st.button("🔍 Get Weather Prediction", use_container_width=True):
    
    if not city:
        st.error("❌ Please enter a city name")
    else:
        try:
            # Fetch weather data from API
            url = f"https://api.openweathermap.org/data/2.5/weather?q={city}&appid={API_KEY}&units=metric"
            response = requests.get(url, timeout=5)
            data = response.json()

            if data.get("cod") == 200:
                # Extract weather data
                temp = data["main"]["temp"]
                feels_like = data["main"]["feels_like"]
                humidity = data["main"]["humidity"]
                pressure = data["main"]["pressure"]
                weather = data["weather"][0]["description"]
                wind_speed = data["wind"]["speed"]
                cloudiness = data["clouds"]["all"]
                lat = data["coord"]["lat"]
                lon = data["coord"]["lon"]
                country = data["sys"]["country"]

                # Display success message with weather data
                st.success(f"✅ Weather Found for {city}, {country}")
                st.markdown("---")

                # Display weather information in columns
                col1, col2, col3 = st.columns(3)
                col1.metric("🌡 Temperature", f"{temp}°C", f"Feels like {feels_like}°C")
                col2.metric("💧 Humidity", f"{humidity}%")
                col3.metric("💨 Wind Speed", f"{wind_speed} m/s")

                col4, col5, col6 = st.columns(3)
                col4.metric("☁️ Cloudiness", f"{cloudiness}%")
                col5.metric("🔽 Pressure", f"{pressure} hPa")
                col6.metric("📍 Coordinates", f"{lat:.2f}, {lon:.2f}")

                st.markdown("---")
                st.subheader(f"☁️ Weather Condition: {weather.capitalize()}")

                # Direct public links
                st.markdown("### 🔗 View on Map")
                col1, col2 = st.columns(2)
                with col1:
                    openweather_link = f"https://openweathermap.org/find?q={city}"
                    st.link_button("📍 OpenWeatherMap", openweather_link, use_container_width=True)
                with col2:
                    google_maps_link = f"https://www.google.com/maps?q={lat},{lon}"
                    st.link_button("🗺️ Google Maps", google_maps_link, use_container_width=True)

            else:
                st.error(f"❌ City not found! Please check the spelling and try again.")
                st.info("💡 Try cities like: London, New York, Mumbai, Tokyo, Paris, Sydney")

        except requests.exceptions.Timeout:
            st.error("❌ Request timed out. Please check your internet connection and try again.")
        except requests.exceptions.ConnectionError:
            st.error("❌ Connection error. Please check your internet connection.")
        except Exception as e:
            st.error(f"❌ Error: {str(e)}")

st.markdown("---")
st.markdown("**💡 How to use:**")
st.markdown("""
1. Type a city name in the input field
2. Click "Get Weather Prediction"
3. View accurate weather data instantly
4. Click links to view on OpenWeatherMap or Google Maps
""")
##  streamlit run app.py
