import React, { useState, useEffect } from 'react';
import { ReactComponent as YellowPawn } from 'assets/icons/yellow_pawn.svg';
import { ReactComponent as RedPawn } from 'assets/icons/red_pawn.svg';
import { ReactComponent as BluePawn } from 'assets/icons/blue_pawn.svg';
import { ReactComponent as GreenPawn } from 'assets/icons/green_pawn.svg';
import { StyledBox } from './Box.styles';

const Box = (props) => {
  const [pawnsObject, setPawnsArray] = useState(props.pawnsArray);
  const [pawnToBeDisplayed, setPawnsToBeDisplayed] = useState([]);
  const [loadingState, setLoadingState] = useState(false);

  useEffect(() => {
    // console.log('Pawns Array in Box component: ', pawnsArray);
  }, []);

  useEffect(() => {
    setPawnsArray(props.pawnsArray);
  }, [props.pawnsArray]);

  useEffect(() => {
    if (pawnsObject) {
      findPawnsToBeDisplayed();
      if (pawnsObject) {
        console.log('Pawns Object in Box component: ', pawnsObject);
      }
    }
  }, [pawnsObject]);

  useEffect(() => {
    renderSwitch(pawnToBeDisplayed);
  }, [pawnToBeDisplayed]);

  const findPawnsToBeDisplayed = () => {
    if (pawnsObject) {
      for (const [key, value] of Object.entries(pawnsObject)) {
        if (props.id === value.location) {
          // FIXME - BASE fields
          setPawnsToBeDisplayed(value.color);
          console.log(`Pawn to be displayed: ', ${value.color}, ${value.location}, key: ${key}`);
        }
      }
    }
  };

  const renderSwitch = () => {
    switch (pawnToBeDisplayed) {
      case 'YELLOW':
        return (
          <YellowPawn title={props.id} id={props.id} style={{ backgroundColor: 'darkyellow', width: '50px', height: '50px', left: '0', right: '0', transform: 'scale(70%)' }} />
        );
      case 'BLUE':
        return <BluePawn title={props.id} id={props.id} style={{ backgroundColor: 'darkblue', width: '50px', height: '50px', left: '0', right: '0', transform: 'scale(70%)' }} />;
      case 'GREEN':
        return <GreenPawn title={props.id} id={props.id} style={{ backgroundColor: 'darkgreen', width: '50px', height: '50px', left: '0', right: '0', transform: 'scale(70%)' }} />;
      case 'RED':
        return <RedPawn title={props.id} id={props.id} style={{ backgroundColor: 'darkred', width: '50px', height: '50px', left: '0', right: '0', transform: 'scale(70%)' }} />;
      default:
        break;
    }
  };

  return <StyledBox {...props}>{renderSwitch()}</StyledBox>;
};

export default Box;
