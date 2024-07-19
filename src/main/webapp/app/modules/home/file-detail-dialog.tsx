import React, { useEffect, useState } from 'react';
import { Button, Row, Col, Modal, ModalHeader, ModalBody, ModalFooter } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import axios from 'axios';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntity } from 'app/entities/file/file.reducer';
import { Link } from 'react-router-dom';

interface FileDetailDialogProps {
  isOpen: boolean;
  toggle: () => void;
  fileId: number | null;
}

export const FileDetailDialog: React.FC<FileDetailDialogProps> = ({ isOpen, toggle, fileId }) => {
  const dispatch = useAppDispatch();
  const [fileUrl, setFileUrl] = useState<string | null>(null);
  const [mediaType, setMediaType] = useState<string>('');
  const [loading, setLoading] = useState<boolean>(false);

  const fileEntity = useAppSelector(state => state.file.entity);

  useEffect(() => {
    if (fileId) {
      dispatch(getEntity(fileId));

      setLoading(true);
      axios
        .get(`/api/download/file/${fileId}`, { responseType: 'blob' })
        .then(response => {
          const contentType = response.headers['content-type'];
          setMediaType(contentType);

          const url = URL.createObjectURL(new Blob([response.data], { type: contentType }));
          setFileUrl(url);
        })
        .catch(error => {
          console.error('Error fetching file:', error);
          setFileUrl(null); // Handle error scenario
        })
        .finally(() => {
          setLoading(false);
        });
    }
  }, [fileId, dispatch]);

  const handleDownloadWordDoc = () => {
    if (fileId) {
      setLoading(true);
      axios
        .get(`/api/download/file/${fileId}`, { responseType: 'blob' })
        .then(response => {
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
        })
        .finally(() => {
          setLoading(false);
        });
    }
  };

  const handleViewPdf = () => {
    if (fileId) {
      setLoading(true);
      axios
        .get(`/api/download/file/${fileId}`, { responseType: 'blob' })
        .then(response => {
          const url = URL.createObjectURL(new Blob([response.data], { type: 'application/pdf' }));
          window.open(url, '_blank');
        })
        .catch(error => {
          console.error('Error viewing PDF:', error);
        })
        .finally(() => {
          setLoading(false);
        });
    }
  };

  const renderFileContent = () => {
    if (!fileUrl) {
      return <div>No preview available</div>;
    }

    if (mediaType.startsWith('application')) {
      if (mediaType === 'application/pdf') {
        return (
          <Button color="primary" onClick={handleViewPdf}>
            View {fileEntity.name}
          </Button>
        );
      } else if (mediaType === 'application/vnd.openxmlformats-officedocument.wordprocessingml.document') {
        return (
          <Button color="primary" onClick={handleDownloadWordDoc}>
            Download {fileEntity.name}
          </Button>
        );
      } else {
        return <div>Unsupported application file type</div>;
      }
    }

    if (mediaType.startsWith('image')) {
      return <img src={fileUrl} alt={fileEntity.name} style={{ maxWidth: '100%', height: 'auto' }} />;
    }

    if (mediaType.startsWith('video')) {
      return (
        <video controls width="100%" height="auto">
          <source src={fileUrl} />
          Your browser does not support the video tag.
        </video>
      );
    }

    if (mediaType.startsWith('text')) {
      return <iframe src={fileUrl} style={{ width: '100%', height: '500px' }} title="Text Viewer" />;
    }

    return <div>Unsupported file type</div>;
  };

  return (
    <Modal isOpen={isOpen} toggle={toggle}>
      <ModalHeader toggle={toggle}>{fileEntity.name}</ModalHeader>
      <ModalBody>
        <Row>
          <div className="modal-body">
            <div>
              <dt>Folder</dt>
              <dd>{fileEntity.foldername}</dd>
            </div>
            <hr />
            <div>
              <dt>Type</dt>
              <dd>{fileEntity.typeName}</dd>
            </div>
            <hr />
            <div>
              <dt>
                <span id="date">Added</span>
              </dt>
              <dd>{fileEntity.added ? <TextFormat value={fileEntity.added} type="date" format={APP_DATE_FORMAT} /> : 'N/A'}</dd>
            </div>
            <hr />
          </div>
          <div className="jh-entity-details">
            <dt>
              <span id="file">File</span>
            </dt>
            <dd style={{ paddingTop: '20px' }}>{loading ? <div>Loading...</div> : renderFileContent()}</dd>
          </div>
        </Row>
      </ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={toggle}>
          Close
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default FileDetailDialog;
