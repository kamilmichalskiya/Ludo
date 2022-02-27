import React from 'react';
import PropTypes from 'prop-types';
import { StyledAverage, StyledInfo, Wrapper } from './GamesListItem.styles';
import { Button } from 'components/atoms/Button/Button';
import { Label } from 'components/atoms/Label/Label';

const GamesListItem = ({ index, userData: { id, status = 'NEW', players }, joinGame, userName }) => {
  const isUserPlaying = () => {
    if (players && userName) {
      for (const player of players) {
        if (player.nick === userName) {
          return true;
        }
      }
    }
    return false;
  };

  return (
    <Wrapper>
      <StyledAverage value={status} players={players.length}>{`${players.length}/4`}</StyledAverage>
      <StyledInfo>
        <Label>GameId: {id}</Label>
        <br />
        <Label>Game status: {status}</Label>
        <br />
        {}
        <Button disabled={!(status === 'NEW' && players.length < 4) && !isUserPlaying()} onClick={() => joinGame(id, index)}>
          Join Game
        </Button>
      </StyledInfo>
    </Wrapper>
  );
};

GamesListItem.propTypes = {
  userData: PropTypes.shape({
    average: PropTypes.string,
    name: PropTypes.string,
    gameStatus: PropTypes.string,
  }),
};

export default GamesListItem;
