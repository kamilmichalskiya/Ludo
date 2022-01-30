import { createGlobalStyle } from 'styled-components';

export const GlobalStyle = createGlobalStyle`
 @import url('https://fonts.googleapis.com/css2?family=Montserrat:wght@400;700&display=swap'); // wzięte z google fonts


 html { // border-box jest znacznie bardziej przewidywalny niż content-Box
  box-sizing: border-box;
 }

 *, *::after, *::before { // by wszystkie elementy i pseudoelementy dziedziczyły box-sizing
  box-sizing: inherit;
 }

 body {
  font-family: 'Montserrat', sans-serif;
 }

 a, button { // linki i buttony nie dziedziczą z body font-family
  font-family: 'Montserrat', sans-serif;
 }
`;
