import React, { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import axios from 'axios';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntity } from './file.reducer';

export const FileDetail = () => {
  const dispatch = useAppDispatch();
  const { id } = useParams<'id'>();
  const [fileUrl, setFileUrl] = useState<string | null>(null);
  const [mediaType, setMediaType] = useState<string>('');
  const [isWordDocument, setIsWordDocument] = useState<boolean>(false);

  useEffect(() => {
    dispatch(getEntity(id));

    axios
      .get(`/api/download/file/${id}`, { responseType: 'blob' })
      .then(response => {
        const contentType = response.headers['content-type'];
        const mediaType = contentType.split('/')[0]; // 'application', 'image', 'video', 'text', etc.
        setMediaType(contentType);

        // For other types, display directly
        const url = URL.createObjectURL(new Blob([response.data], { type: contentType }));
        setFileUrl(url);
      })
      .catch(error => {
        console.error('Error fetching file:', error);
        setFileUrl(null); // Handle error scenario
      });
  }, [id, dispatch]);

  const fileEntity = useAppSelector(state => state.file.entity);

  const handleDownloadWordDoc = () => {
    axios
      .get(`/api/download/file/${id}`, { responseType: 'blob' })
      .then(response => {
        // Create a blob URL and trigger download
        const url = URL.createObjectURL(
          new Blob([response.data], { type: 'application/vnd.openxmlformats-officedocument.wordprocessingml.document' })
        );
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', `${fileEntity.name}.docx`);
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
      })
      .catch(error => {
        console.error('Error downloading file:', error);
      });
  };

  const handleViewPdf = () => {
    axios
      .get(`/api/download/file/${id}`, { responseType: 'blob' })
      .then(response => {
        // Create a blob URL and open in a new tab/window
        const url = URL.createObjectURL(new Blob([response.data], { type: 'application/pdf' }));
        window.open(url, '_blank');
      })
      .catch(error => {
        console.error('Error viewing PDF:', error);
      });
  };

  return (
    <Row>
      <Col md="8">
        <h2 data-cy="fileDetailsHeading">File</h2>
        <dl className="jh-entity-details">
          <dt>Folder</dt>
          <dd>{fileEntity.foldername}</dd>
          <dt>File Type</dt>
          <dd>{fileEntity.typeName}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{fileEntity.name}</dd>
          <dt>
            <span id="date">Added</span>
          </dt>
          <dd>{fileEntity.added ? <TextFormat value={fileEntity.added} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="name">File</span>
          </dt>
          <dd>
            {fileUrl &&
              (mediaType.startsWith('application') ? (
                mediaType === 'application/pdf' ? (
                  <Button color="primary" onClick={handleViewPdf}>
                    View {fileEntity.name}
                  </Button>
                ) : fileEntity.typeName.startsWith('doc') ? (
                  <Button color="primary" onClick={handleDownloadWordDoc}>
                    Download {fileEntity.name}
                  </Button>
                ) : (
                  <div>Unsupported application file type</div>
                )
              ) : fileEntity.typeName.startsWith('doc') ? (
                <Button color="primary" onClick={handleDownloadWordDoc}>
                  Download {fileEntity.name}
                </Button>
              ) : mediaType.startsWith('image') ? (
                <img src={fileUrl} alt={fileEntity.name} style={{ maxWidth: '100%', height: 'auto' }} />
              ) : mediaType.startsWith('video') ? (
                <video controls width="100%" height="auto">
                  <source src={fileUrl} />
                  Your browser does not support the video tag.
                </video>
              ) : mediaType.startsWith('text') ? (
                <iframe src={fileUrl} style={{ width: '100%', height: '500px' }} title="CSV Viewer" />
              ) : (
                <div>Unsupported file type</div>
              ))}
          </dd>
        </dl>
        <Button tag={Link} to="/file" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/file/${fileEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default FileDetail;
