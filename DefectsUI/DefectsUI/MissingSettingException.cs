﻿using System.Runtime.Serialization;

namespace DefectsUI
{
    [Serializable]
    internal class MissingSettingException : Exception
    {
        public MissingSettingException()
        {
        }

        public MissingSettingException(string? message) : base(message)
        {
        }

        public MissingSettingException(string? message, Exception? innerException) : base(message, innerException)
        {
        }

        protected MissingSettingException(SerializationInfo info, StreamingContext context) : base(info, context)
        {
        }
    }
}