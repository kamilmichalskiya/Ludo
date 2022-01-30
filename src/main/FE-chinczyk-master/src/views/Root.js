import React from 'react';
import GameList from 'components/organisms/GamesList/GamesList';
import { ThemeProvider } from 'styled-components';
import { GlobalStyle } from 'assets/styles/GlobalStyle';
import { theme } from 'assets/styles/theme';
import { Wrapper } from './Root.styles';
import { BrowserRouter as Router, Switch, Route } from 'react-router-dom';
import GamesBoard from 'components/organisms/GameBoard/GameBoard';

const Root = () => {
  return (
    <Router>
      <ThemeProvider theme={theme}>
        <GlobalStyle />
        <Wrapper>
          {/* <nav>
            <Link to="/">Home</Link>
            <Link to="/game-board">Enter Game</Link>
          </nav> */}
          <Switch>
            <Route path="/game-board" render={(props) => <GamesBoard {...props} />}></Route>
            <Route path="/">
              <GameList />
            </Route>
          </Switch>
        </Wrapper>
      </ThemeProvider>
    </Router>
  );
};

export default Root;
