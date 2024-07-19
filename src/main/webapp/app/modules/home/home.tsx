import './home.scss';

import React from 'react';
import { Link } from 'react-router-dom';

import { Row, Col, Alert } from 'reactstrap';

import { useAppSelector } from 'app/config/store';
import Folder from './folder';
import File from './file';

export const Home = () => {
  return (
    <Row>
      <Col md="12">
        <Folder></Folder>
      </Col>
    </Row>
  );
};

export default Home;
